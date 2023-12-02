package jie.runtime.net.sockets;

import jie.runtime.net.sockets.event.ISocketClientEvent;
import jie.runtime.net.sockets.event.SocketDataEventArgs;
import jie.runtime.net.sockets.event.SocketEventArgs;
import jie.runtime.net.sockets.event.SocketExceptionEventArgs;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 提供套接字客户端服务的类, 该类是抽象的
 *
 * @author jiegg
 */
public abstract class SocketClient<T> {

    //region --字段--
    protected final CopyOnWriteArrayList<ISocketClientEvent> listener;
    //endregion

    //region --属性--

    /**
     * 获取基础网络套接字
     */
    public abstract T getClient();

    /**
     * 获取当前网络客户端的本地 IP 地址
     */
    public abstract InetSocketAddress getLocalAddress();

    /**
     * 获取当前网络客户端的远程 IP 地址
     */
    public abstract InetSocketAddress getRemoteAddress();

    /**
     * 获取当前客户端是否正在运行
     */
    public abstract boolean isRunning();

    /**
     * 获取当前客户端是否已连接到远程服务器
     */
    public abstract boolean isConnected();

    /**
     * 获取当前客户端接收或发送数据时的封包大小
     */
    public abstract int getPacketSize();

    /**
     * 设置当前客户端接收或发送数据时的封包大小
     *
     * @param value 一个 32 位整数, 指定封包大小
     */
    public abstract void setPacketSize(int value);

    /**
     * 获取当前客户端接收或发送数据时的封包头占用字节数
     */
    public abstract byte getPacketHeaderLength();
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketClient} 类的新实例
     */
    public SocketClient() {
        this.listener = new CopyOnWriteArrayList<>();
    }
    //endregion

    //region --公开方法--

    /**
     * 连接远程客户端
     *
     * @param remoteAddr 远程服务端点
     */
    public abstract void connect(InetSocketAddress remoteAddr);

    /**
     * 断开连接远程客户端
     *
     * @param reuseClient 是否需要复用当前客户端
     */
    public abstract void disconnect(boolean reuseClient);

    /**
     * 发送数据到远程客户端
     *
     * @param data 要发送的数据
     */
    public abstract void send(byte[] data);

    /**
     * 释放当前实例所占用的资源
     */
    public void close() {
        this.listener.clear();
    }

    /**
     * 添加当前客户端事件发生时的监听器
     *
     * @param listener {@link ISocketClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void addListener(ISocketClientEvent listener) {
        if (!this.listener.contains(listener)) {
            this.listener.add(listener);
        }
    }

    /**
     * 移除当前客户端事件发生时的监听器
     *
     * @param listener {@link ISocketClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void removeListener(ISocketClientEvent listener) {
        this.listener.remove(listener);
    }
    //endregion

    //region --私有方法--
    protected void invokeConnectedEvent() {
        for (ISocketClientEvent item : this.listener) {
            if (item != null) {
                item.onConnected(this, new SocketEventArgs());
            }
        }
    }

    protected void invokeDisconnectedEvent() {
        for (ISocketClientEvent item : this.listener) {
            if (item != null) {
                item.onDisconnected(this, new SocketEventArgs());
            }
        }
    }

    protected void invokeReceiveDataEvent(byte[] data) {
        for (ISocketClientEvent item : this.listener) {
            if (item != null) {
                item.onReceiveData(this, new SocketDataEventArgs(data));
            }
        }
    }

    protected void invokeSendDataEvent(byte[] data) {
        for (ISocketClientEvent item : this.listener) {
            if (item != null) {
                item.onSendData(this, new SocketDataEventArgs(data));
            }
        }
    }

    protected void invokeExceptionEvent(Throwable e) {
        for (ISocketClientEvent item : this.listener) {
            if (item != null) {
                item.onException(this, new SocketExceptionEventArgs(e));
            }
        }
    }
    //endregion
}
