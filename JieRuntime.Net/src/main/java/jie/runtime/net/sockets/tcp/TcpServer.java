package jie.runtime.net.sockets.tcp;

import jie.runtime.net.sockets.*;
import jie.runtime.net.sockets.event.ISocketClientEvent;
import jie.runtime.net.sockets.event.SocketDataEventArgs;
import jie.runtime.net.sockets.event.SocketEventArgs;
import jie.runtime.net.sockets.event.SocketExceptionEventArgs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 提供基于 TCP 协议的网络服务端
 *
 * @author jiegg
 */
public class TcpServer extends SocketServer<AsynchronousServerSocketChannel, SocketClient<AsynchronousSocketChannel>> {

    //region --字段--
    private AsynchronousServerSocketChannel server;
    private final InetSocketAddress listenerAddress;
    private int listenBacklog;
    private boolean isRunning;

    private final AcceptHandler acceptHandler;
    private final ClientEventHandler clientEventHandler;
    //endregion

    //region --属性--

    /**
     * 获取基础网络套接字 {@link AsynchronousServerSocketChannel}
     */
    @Override
    public AsynchronousServerSocketChannel getServer() {
        return this.server;
    }

    /**
     * 获取当前网络服务端监听的 IP 地址
     */
    @Override
    public InetSocketAddress getLocalAddress() {
        return this.listenerAddress;
    }

    /**
     * 获取当前服务端挂起连接队列的最大长度
     */
    @Override
    public int getListenBacklog() {
        return this.listenBacklog;
    }

    /**
     * 设置当前服务端挂起连接队列的最大长度
     */
    @Override
    public void setListenBacklog(int value) throws SocketException {
        if (this.isRunning()) {
            throw new SocketException("无法设置监听队列长度, 因为服务正在运行");
        }
        this.listenBacklog = value;
    }

    /**
     * 获取当前服务端是否正在运行
     */
    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link TcpServer} 类的新实例, 网络服务端将监听本地地址
     *
     * @param port 服务端使用的端口
     * @throws UnknownHostException 获取本地地址失败
     */
    public TcpServer(int port) throws UnknownHostException {
        this(InetAddress.getLocalHost(), port);
    }

    /**
     * 使用指定的 IP 地址和端口初始化 {@link TcpServer} 类的新实例
     *
     * @param localAddr 本地 IP 地址
     * @param port      服务端使用的端口号
     */
    public TcpServer(InetAddress localAddr, int port) {
        if (localAddr == null) {
            throw new NullPointerException("参数: localAddr 为 null");
        }

        // 监听地址
        this.listenerAddress = new InetSocketAddress(localAddr, port);
        this.listenBacklog = Integer.MAX_VALUE;

        // 创建处理器
        this.acceptHandler = new AcceptHandler();
        this.clientEventHandler = new ClientEventHandler();
    }
    //endregion

    //region --公开方法--

    /**
     * 启动服务端
     */
    @Override
    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;

            try {
                // 创建监听套接字
                if (this.server == null) {
                    this.server = AsynchronousServerSocketChannel.open();
                }

                // 绑定监听地址
                this.server.bind(this.getLocalAddress(), this.getListenBacklog());

                // 触发事件
                this.invokeStartedEvent();

                // 开始监听
                this.server.accept(null, this.acceptHandler);

            } catch (IOException e) {
                this.invokeExceptionEvent(e);
            }
        }
    }

    /**
     * 停止服务端
     */
    @Override
    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;

            try {
                // 关闭套接字
                if (this.server != null) {
                    this.server.close();
                }

                // 释放并关闭所有客户端
                for (SocketClient<?> client : this.getClients()) {
                    client.close();
                }
                this.listener.clear();

                // 触发事件
                this.invokeStoppedEvent();

            } catch (IOException e) {
                this.invokeExceptionEvent(e);
            }
        }
    }

    /**
     * 释放当前实例所占用的资源
     */
    @Override
    public void close() {
        this.stop();
    }
    //endregion

    //region --内部类--

    /**
     * 服务器等待处理器
     */
    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
            if (TcpServer.this.isRunning() && result != null) {
                try {
                    // 托管 socket 客户端
                    TcpClient client = new TcpClient(result);
                    client.addListener(TcpServer.this.clientEventHandler);

                    // 加入托管队列
                    TcpServer.this.clients.add(client);

                    // 调用连接事件
                    TcpServer.this.invokeClientConnectedEvent(client);
                } catch (Exception e) {
                    TcpServer.this.invokeExceptionEvent(e);
                } finally {
                    // 继续等待连接
                    if (TcpServer.this.isRunning()) {
                        if (TcpServer.this.server != null) {
                            TcpServer.this.server.accept(null, TcpServer.this.acceptHandler);
                        }
                    }
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            TcpServer.this.invokeExceptionEvent(exc);
        }
    }

    /**
     * 客户端事件处理器
     */
    private class ClientEventHandler implements ISocketClientEvent {

        /**
         * 表示客户端成功连接到远程服务器的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   事件参数
         */
        @Override
        public void onConnected(Object sender, SocketEventArgs args) {

        }

        /**
         * 表示客户端断开与远程服务器连接的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   事件参数
         */
        @Override
        public void onDisconnected(Object sender, SocketEventArgs args) {
            if (sender instanceof SocketClient<?>) {
                SocketClient<?> client = (SocketClient<?>) sender;

                // 移除事件处理器
                client.removeListener(this);

                // 移除客户端
                TcpServer.this.clients.remove(client);

                // 调用客户端断开连接事件
                TcpServer.this.invokeClientDisConnectedEvent(client);
            }
        }

        /**
         * 表示客户端收到远程服务器数据的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   数据事件参数
         */
        @Override
        public void onReceiveData(Object sender, SocketDataEventArgs args) {
            if (sender instanceof SocketClient<?>) {
                SocketClient<?> client = (SocketClient<?>) sender;
                TcpServer.this.invokeReceiveDataEvent(client, args.getData());
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
            if (sender instanceof SocketClient<?>) {
                SocketClient<?> client = (SocketClient<?>) sender;
                TcpServer.this.invokeSendDataEvent(client, args.getData());
            }
        }

        /**
         * 表示套接字客户端出现异常的事件
         *
         * @param sender 引发此事件的事件源
         * @param args   异常事件参数
         */
        @Override
        public void onException(Object sender, SocketExceptionEventArgs args) {

        }
    }
    //endregion
}
