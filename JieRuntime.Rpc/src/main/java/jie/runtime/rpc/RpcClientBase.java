package jie.runtime.rpc;

import jie.runtime.rpc.event.IRpcClientEvent;
import jie.runtime.rpc.event.RpcEventArgs;
import jie.runtime.rpc.event.RpcExceptionEventArgs;
import jie.runtime.rpc.proxy.ProxyBase;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 提供远程调用服务客户端的基础类, 该类是抽象的
 *
 * @author jiegg
 */
public abstract class RpcClientBase extends ProxyBase {

    //region --字段--
    private final Map<String, RpcService> services;
    private Duration waitResponseTime;
    private final CopyOnWriteArrayList<IRpcClientEvent> listener;

    /**
     * 类型转换字典
     */
    protected static final Map<RpcClientType, RpcTypeConverter> TYPE_CONVERTER_MAP;
    protected static final Map<RpcClientType, RpcMethodConverter> METHOD_CONVERTER_MAP;
    //endregion

    //region --属性--

    /**
     * 获取一个 {@link Boolean} 值, 指示当前客户端是否正在运行
     */
    public abstract boolean isRunning();

    /**
     * 获取当前客户端的类型
     */
    public RpcClientType getType() {
        return RpcClientType.Java;
    }

    /**
     * 获取当前远程调用客户端已注册的服务
     */
    public Map<String, RpcService> getServices() {
        return Collections.unmodifiableMap(this.services);
    }

    /**
     * 获取当前远程调用客户端的等待响应时间
     */
    public Duration getWaitResponseTime() {
        return this.waitResponseTime;
    }

    /**
     * 设置当前远程调用客户端的等待响应时间
     *
     * @param value 等待时长
     */
    public void setWaitResponseTime(Duration value) {
        if (value == null) {
            throw new NullPointerException("参数: value 是 null");
        }
        this.waitResponseTime = value;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link RpcClientBase} 类的新实例
     */
    public RpcClientBase() {
        this.services = new HashMap<>();
        this.waitResponseTime = Duration.ofSeconds(10);

        this.listener = new CopyOnWriteArrayList<>();
    }

    static {
        TYPE_CONVERTER_MAP = new HashMap<>();
        TYPE_CONVERTER_MAP.put(RpcClientType.Java, new RpcJavaTypeConverter());
        TYPE_CONVERTER_MAP.put(RpcClientType.CSharp, new RpcCSharpTypeConverter());

        METHOD_CONVERTER_MAP = new HashMap<>();
        METHOD_CONVERTER_MAP.put(RpcClientType.Java, new RpcJavaMethodConverter());
    }
    //endregion

    //region --公开方法--

    /**
     * 注册远程调用服务实例
     *
     * @param tClass 指定远程调用服务实例的接口类型
     * @param obj    远程调用服务实例
     * @param <T>    指定远程调用服务实例的接口类型
     */
    public <T> void register(Class<T> tClass, T obj) {
        if (tClass == null) {
            throw new NullPointerException("参数: tClass 是 null");
        }

        if (obj == null) {
            throw new NullPointerException("参数: obj 是 null");
        }

        if (!tClass.isInterface()) {
            throw new IllegalArgumentException("参数: tClass, 类型 (" + tClass.getSimpleName() + ") 不是接口类型");
        }

        if (!this.services.containsKey(tClass.getSimpleName())) {
            this.services.put(tClass.getSimpleName(), new RpcService(tClass, obj));
        }
    }

    /**
     * 取消注册远程调用服务实例
     *
     * @param tClass 指定远程调用服务实例的接口类型
     */
    public void unregister(Class<?> tClass) {
        if (tClass == null) {
            throw new NullPointerException("参数: tClass 是 null");
        }

        if (!tClass.isInterface()) {
            throw new IllegalArgumentException("参数: tClass, 类型 (" + tClass.getSimpleName() + ") 不是接口类型");
        }

        this.services.remove(tClass.getSimpleName());
    }

    /**
     * 连接到远程调用服务端
     */
    public abstract void connect();

    /**
     * 断开与远程调用服务端的连接
     */
    public abstract void disconnect();

    /**
     * 添加当前客户端事件发生时的监听器
     *
     * @param listener {@link IRpcClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void addListener(IRpcClientEvent listener) {
        if (!this.listener.contains(listener)) {
            this.listener.add(listener);
        }
    }

    /**
     * 移除当前客户端事件发生时的监听器
     *
     * @param listener {@link IRpcClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void removeListener(IRpcClientEvent listener) {
        this.listener.remove(listener);
    }
    //endregion

    //region --私有方法--

    /**
     * 向远程调用服务端发送数据, 并等待服务端的回应
     *
     * @param tag  指定数据的唯一标识
     * @param data 要发送的数据
     * @return 一个字节数组, 包含服务端回应的数据
     */
    protected abstract byte[] sendWaitResponse(long tag, byte[] data) throws Exception;

    /**
     * 向远程调用服务端发送数据, 以响应远程调用服务端的请求
     *
     * @param tag  指定数据的唯一标识
     * @param data 要发送的数据
     */
    protected abstract void sendResponse(long tag, byte[] data) throws IOException;

    protected void invokeConnectedEvent() {
        for (IRpcClientEvent item : this.listener) {
            if (item != null) {
                item.onConnected(this, new RpcEventArgs());
            }
        }
    }

    protected void invokeDisconnectedEvent() {
        for (IRpcClientEvent item : this.listener) {
            if (item != null) {
                item.onDisconnected(this, new RpcEventArgs());
            }
        }
    }

    protected void invokeExceptionEvent(Throwable e) {
        for (IRpcClientEvent item : this.listener) {
            if (item != null) {
                item.onException(this, new RpcExceptionEventArgs(e));
            }
        }
    }
    //endregion
}
