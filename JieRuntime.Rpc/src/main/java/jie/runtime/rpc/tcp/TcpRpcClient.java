package jie.runtime.rpc.tcp;

import com.alibaba.fastjson2.JSONException;
import jie.runtime.net.sockets.event.ISocketClientEvent;
import jie.runtime.net.sockets.event.SocketDataEventArgs;
import jie.runtime.net.sockets.event.SocketEventArgs;
import jie.runtime.net.sockets.event.SocketExceptionEventArgs;
import jie.runtime.net.sockets.tcp.TcpClient;
import jie.runtime.rpc.RpcClientBase;
import jie.runtime.rpc.RpcMethodConverter;
import jie.runtime.rpc.RpcTypeConverter;
import jie.runtime.rpc.util.JsonUtils;
import jie.runtime.utils.GuidUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.LockSupport;

/**
 * 基于 TCP 协议提供远程调用服务客户端
 *
 * @author jiegg
 */
public class TcpRpcClient extends RpcClientBase {

    //region --字段--
    private InetSocketAddress remoteAddr;
    private final TcpClient client;
    private final GenericObjectPool<TcpWait> waitPool;
    private final ConcurrentMap<Long, TcpWait> waitReference;
    private final FragmentCache fragmentCache;
    //endregion

    //region --属性--

    /**
     * 获取一个 {@link Boolean} 值, 指示当前客户端是否正在运行
     */
    @Override
    public boolean isRunning() {
        if (this.client == null) {
            return false;
        }
        return this.client.isRunning();
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link TcpRpcClient} 类的新实例
     *
     * @param remoteAddr 指定远程服务端的地址
     */
    public TcpRpcClient(InetSocketAddress remoteAddr) {
        this(new TcpClient());
        if (remoteAddr == null) {
            throw new NullPointerException("参数: remoteAddr 是 null");
        }
        this.remoteAddr = remoteAddr;
    }

    TcpRpcClient(TcpClient client) {
        // 创建客户端
        if (client == null) {
            this.client = new TcpClient();
        } else {
            this.client = client;
        }

        this.client.setPacketSize(TcpClient.DEFAULT_PACKET_SIZE);
        this.client.addListener(new TcpClientEventHandler());

        // 创建对象池
        this.waitPool = new GenericObjectPool<>(new TcpWaitObjectFactory());
        this.waitReference = new ConcurrentHashMap<>();

        this.fragmentCache = new FragmentCache();
    }
    //endregion

    //region --公开方法--

    /**
     * 连接到远程调用服务端
     */
    @Override
    public void connect() {
        if (this.client != null) {
            this.client.connect(this.remoteAddr);
        }
    }

    /**
     * 断开与远程调用服务端的连接
     */
    @Override
    public void disconnect() {
        if (this.client != null) {
            this.client.disconnect(true);
        }
    }

    /**
     * 向远程调用服务端发送数据, 并等待服务端的回应
     *
     * @param tag  指定数据的唯一标识
     * @param data 要发送的数据
     * @return 一个字节数组, 包含服务端回应的数据
     */
    @Override
    protected byte[] sendWaitResponse(long tag, byte[] data) throws Exception {
        if (!this.waitReference.containsKey(tag)) {
            try {
                // 获取 TCP 等待对象
                TcpWait wait = this.waitPool.borrowObject();

                // 绑定 Tag 对象
                this.waitReference.put(tag, wait);

                // 设置等待的线程为自身线程
                wait.setWaitThread(Thread.currentThread());

                // TODO 加密数据
                byte[] encryptData = data;

                // 发送消息
                this.send(new Packet(PacketType.REQUEST, tag, encryptData));

                // 等待消息返回
                if (this.getWaitResponseTime().isZero()) {
                    LockSupport.park();
                } else {
                    LockSupport.parkNanos(this.getWaitResponseTime().toNanos());
                }
                // 如果结果是 null 并且获取过响应状态, 表示传输出现了问题
                if (wait.getResult() == null && wait.isResponse()) {
                    throw new JsonRpcNetworkException();
                }

                byte[] receiveData = wait.getResult();

                // TODO 解密数据

                return receiveData;

            } finally {
                TcpWait wait = this.waitReference.remove(tag);
                // 归还对象
                this.waitPool.returnObject(wait);
            }
        }
        return null;
    }

    /**
     * 向远程调用服务端发送数据, 以响应远程调用服务端的请求
     *
     * @param tag  指定数据的唯一标识
     * @param data 要发送的数据
     */
    @Override
    protected void sendResponse(long tag, byte[] data) throws IOException {
        // TODO 加密数据
        byte[] encryptData = data;

        // 发送数据
        this.send(new Packet(PacketType.RESPONSE, tag, encryptData));
    }

    /**
     * 每当调用代理类型上的任何方法时，都会调用此方法
     *
     * @param targetMethod 调用者调用的方法
     * @param args         调用者传递给方法的参数
     * @return 返回给调用者的对象，void 方法将返回 {@code null}
     */
    @Override
    public Object invokeMethod(Method targetMethod, Object[] args) throws Throwable {

        // 获取方法的基本信息
        Class<?> targetType = targetMethod.getDeclaringClass();
        Parameter[] targetParameters = targetMethod.getParameters();

        // 组装基本请求信息
        JsonRpcRequest request = new JsonRpcRequest();
        request.setType(targetType.getSimpleName());
        request.setMethod(targetMethod.getName());
        request.setClientType(this.getType());
        request.setParameters(new JsonRpcParameter[targetParameters.length]);

        // 填充请求参数
        for (int i = 0; i < args.length; i++) {
            JsonRpcParameter jsonParameter = new JsonRpcParameter();
            jsonParameter.setType(targetParameters[i].getType().getSimpleName());
            jsonParameter.setValue(args[i]);

            request.getParameters()[i] = jsonParameter;
        }

        // 生成请求数据
        byte[] requestBody = JsonUtils.serializeToUtf8Bytes(request);

        // 发送请求
        long tag = GuidUtils.newGuidInt64();
        byte[] responseBody = this.sendWaitResponse(tag, requestBody);

        // 有返回值的请求, 需要等待返回值
        if (responseBody != null) {
            JsonRpcResponse response = JsonUtils.deserialize(responseBody, JsonRpcResponse.class);

            // 如果有错误对象的存在, 则表示远程调用出现的异常
            if (response.getError() != null) {
                // 错误处理
                throw new JsonRpcException(response.getError());
            } else {

                // 使用对端的客户端类型获取类型转换器
                RpcTypeConverter rpcTypeConverter = TYPE_CONVERTER_MAP.get(response.getClientType());

                // 回填参数
                for (int i = 0; i < targetParameters.length; i++) {
                    Class<?> parameterType = targetParameters[i].getType();
                    String responseParameterType = response.getParameters()[i].getType();

                    // 使用类型转换器做类型适配
                    if (rpcTypeConverter != null && rpcTypeConverter.isEquals(parameterType, responseParameterType)) {
                        if (parameterType.getSimpleName().equals(String.class.getSimpleName())) {
                            // (仅Java) 判断是否是字符串
                            args[i] = response.getParameters()[i].getValue();
                        } else {
                            // 如果不是字符串类型, 使用Json序列化
                            args[i] = JsonUtils.deserialize((String) response.getParameters()[i].getValue(), parameterType);
                        }
                    }
                }
            }
        }
        return null;
    }
    //endregion

    //region --私有方法--
    private void send(Packet packet) throws IOException {
        Iterable<Fragment> fragments = FragmentCache.createFragments(packet.getType(), packet.getTag(), packet.getData());
        for (Fragment fragment : fragments) {
            this.client.send(fragment.getBytes());
        }
    }
    //endregion

    //region --内部类--
    class TcpClientEventHandler implements ISocketClientEvent {

        /**
         * 表示客户端成功连接到远程服务器的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   事件参数
         */
        @Override
        public void onConnected(Object sender, SocketEventArgs args) {
            TcpRpcClient.this.invokeConnectedEvent();
        }

        /**
         * 表示客户端断开与远程服务器连接的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   事件参数
         */
        @Override
        public void onDisconnected(Object sender, SocketEventArgs args) {
            TcpRpcClient.this.invokeDisconnectedEvent();
        }

        /**
         * 表示客户端收到远程服务器数据的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   数据事件参数
         */
        @Override
        public void onReceiveData(Object sender, SocketDataEventArgs args) {

            // 将收到的消息处理为消息分片
            Fragment fragment = Fragment.tryParse(args.getData());
            if (fragment != null) {
                // 将消息分片送入缓存重组
                TcpRpcClient.this.fragmentCache.push(fragment);

                Packet packet = null;
                do {
                    packet = TcpRpcClient.this.fragmentCache.pull();
                    if (packet != null) {
                        switch (packet.getType()) {
                            case REQUEST:
                                JsonRpcRequest request = null;
                                JsonRpcResponse response = null;

                                try {
                                    request = JsonUtils.deserialize(packet.getData(), JsonRpcRequest.class);

                                    // 根据类型获取指定的服务
                                    if (TcpRpcClient.this.getServices().containsKey(request.getType())) {

                                        // 获取服务类型对应的类型接口
                                        Class<?> type = TcpRpcClient.this.getServices().get(request.getType()).getType();

                                        // 获取指定的成员
                                        RpcTypeConverter typeConverter = TYPE_CONVERTER_MAP.get(request.getClientType());
                                        RpcMethodConverter methodConverter = METHOD_CONVERTER_MAP.get(request.getClientType());
                                        JsonRpcRequest finalRequest = request;
                                        Optional<Method> methodOptional = Arrays.stream(type.getDeclaredMethods())
                                                .filter(method -> {

                                                    // 使用指定客户端方法转换器比对服务方法和请求方法是否一致
                                                    if (methodConverter != null && methodConverter.isEquals(method, finalRequest.getMethod())) {

                                                        // 获取参数列表
                                                        Parameter[] parameters = method.getParameters();

                                                        // 参数个数判断
                                                        if (parameters.length != finalRequest.getParameters().length) {
                                                            return false;
                                                        }

                                                        // 参数类型判断
                                                        for (int i = 0; i < parameters.length; i++) {

                                                            // 使用指定的客户端类型转换器比服务方法的参数类型是否一致
                                                            if (!typeConverter.isEquals(parameters[i].getType(), finalRequest.getParameters()[i].getType())) {
                                                                return false;
                                                            }
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                })
                                                .findFirst();

                                        // 如果方法存在
                                        if (methodOptional.isPresent()) {

                                            Method method = methodOptional.get();

                                            // 创建传参数组
                                            Object[] invokeArgs = new Object[request.getParameters().length];

                                            // 获取方法中所有参数
                                            Parameter[] parameters = method.getParameters();

                                            // 将 Json 转换为对象
                                            for (int i = 0; i < invokeArgs.length; i++) {

                                                Class<?> parameterType = parameters[i].getType();
                                                invokeArgs[i] = JsonUtils.deserialize((String) request.getParameters()[i].getValue(), parameterType);
                                            }

                                            // 调用方法
                                            Object returnValue = method.invoke(TcpRpcClient.this.getServices().get(request.getType()).getInstance(), invokeArgs);

                                            // 赋值返回值
                                            response = JsonRpcResponse.createResult(returnValue);
                                            response.setParameters(new JsonRpcParameter[invokeArgs.length]);

                                            // 赋值改变的参数
                                            for (int i = 0; i < invokeArgs.length; i++) {
                                                JsonRpcParameter parameter = new JsonRpcParameter();
                                                parameter.setType(parameters[i].getType().getSimpleName());
                                                parameter.setValue(invokeArgs[i]);

                                                response.getParameters()[i] = parameter;
                                            }
                                        } else {
                                            response = JsonRpcResponse.createError(JsonRpcResponseError.createMethodNotFoundError(request.getType(), request.getMethod()));
                                        }
                                    } else {
                                        response = JsonRpcResponse.createError(JsonRpcResponseError.createTypeNotFoundError(request.getType()));
                                    }
                                } catch (JSONException e) {
                                    // 包装成Json解析错误
                                    response = JsonRpcResponse.createError(JsonRpcResponseError.createFormatterError(e));
                                } catch (InvocationTargetException e) {
                                    // 包装成应用异常错误
                                    response = JsonRpcResponse.createError(JsonRpcResponseError.createApplicationError(String.format("在执行方法“%s”时发生了异常", request.getMethod()), e));
                                } catch (IllegalAccessException e) {
                                    // 包装成应用异常错误
                                    response = JsonRpcResponse.createError(JsonRpcResponseError.createApplicationError(String.format("无法执行方法“%s”, 指定的方法没有执行权限", request.getMethod()), e));
                                } catch (Throwable e) {
                                    // 包装成系统错误
                                    response = JsonRpcResponse.createError(JsonRpcResponseError.createSystemError("发生错误", e));
                                }

                                byte[] responseData = null;
                                do {
                                    try {
                                        responseData = JsonUtils.serializeToUtf8Bytes(response);
                                    } catch (Exception e) {
                                        // 发送一个错误防止对端卡住, 这个Json是一定可以被序列化的
                                        response = JsonRpcResponse.createError(JsonRpcResponseError.createSystemError("发生错误", e));
                                    }
                                } while (responseData != null);

                                try {
                                    TcpRpcClient.this.sendResponse(packet.getTag(), responseData);
                                } catch (IOException e) {
                                    TcpRpcClient.this.invokeExceptionEvent(e);
                                }
                                break;
                            case RESPONSE:
                                // 处理对端的 TCP 响应
                                TcpWait wait = TcpRpcClient.this.waitReference.get(packet.getTag());
                                if (wait != null) {
                                    wait.setResponse(true);
                                    wait.setResult(packet.getData());
                                    LockSupport.unpark(wait.getWaitThread());
                                }
                                break;
                        }
                    }

                } while (packet != null);
            }
        }

        /**
         * 表示客户端发送数据到远程服务器的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   数据事件参数
         */
        @Override
        public void onSendData(Object sender, SocketDataEventArgs args) {

        }

        /**
         * 表示套接字客户端出现异常的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   异常事件参数
         */
        @Override
        public void onException(Object sender, SocketExceptionEventArgs args) {
            TcpRpcClient.this.invokeExceptionEvent(args.getException());
        }
    }
    //endregion
}
