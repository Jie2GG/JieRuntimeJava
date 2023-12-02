package jie.runtime.net.sockets;

import jie.runtime.net.sockets.event.*;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 提供套接字服务端服务的类, 该类是抽象的
 *
 * @author jiegg
 */
public abstract class SocketServer<T, TClient extends SocketClient<?>> {

    //region --字段--
    protected final CopyOnWriteArrayList<ISocketServerEvent> listener;

    protected final CopyOnWriteArrayList<TClient> clients;
    //endregion

    //region --属性--

    /**
     * 获取基础网络套接字
     */
    public abstract T getServer();

    /**
     * 获取当前网络服务端监听的 IP 地址
     */
    public abstract InetSocketAddress getLocalAddress();

    /**
     * 获取当前服务端挂起连接队列的最大长度
     */
    public abstract int getListenBacklog();

    /**
     * 设置当前服务端挂起连接队列的最大长度
     */
    public abstract void setListenBacklog(int value) throws SocketException;

    /**
     * 获取当前连接到服务端的客户端
     */
    public List<SocketClient<?>> getClients() {
        return Collections.unmodifiableList(this.clients);
    }

    /**
     * 获取当前服务端是否正在运行
     */
    public abstract boolean isRunning();
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketServer} 类的新实例
     */
    protected SocketServer() {
        this.listener = new CopyOnWriteArrayList<>();
        this.clients = new CopyOnWriteArrayList<>();
    }
    //endregion

    //region --公开方法--

    /**
     * 启动服务端
     */
    public abstract void start();

    /**
     * 停止服务端
     */
    public abstract void stop();

    /**
     * 释放当前实例所占用的资源
     */
    public abstract void close();

    /**
     * 添加当前客户端事件发生时的监听器
     *
     * @param listener {@link ISocketClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void addListener(ISocketServerEvent listener) {
        if (!this.listener.contains(listener)) {
            this.listener.add(listener);
        }
    }

    /**
     * 移除当前客户端事件发生时的监听器
     *
     * @param listener {@link ISocketClientEvent} 监听器, 用于监听当前客户端的事件
     */
    public void remoteListener(ISocketServerEvent listener) {
        this.listener.remove(listener);
    }
    //endregion

    //region --私有方法--
    protected void invokeStartedEvent() {
        if (!this.listener.isEmpty()) {
            for (ISocketServerEvent item : this.listener) {
                if (item != null) {
                    item.onStarted(this, new SocketEventArgs());
                }
            }
        }
    }

    protected void invokeStoppedEvent() {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onStopped(this, new SocketEventArgs());
            }
        }
    }

    protected void invokeReceiveDataEvent(SocketClient<?> client, byte[] data) {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onReceiveData(this, new SocketServerDataEventArgs(client, client.getRemoteAddress(), data));
            }
        }
    }

    protected void invokeSendDataEvent(SocketClient<?> client, byte[] data) {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onSendData(this, new SocketServerDataEventArgs(client, client.getRemoteAddress(), data));
            }
        }
    }

    protected void invokeExceptionEvent(Throwable e) {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onException(this, new SocketExceptionEventArgs(e));
            }
        }
    }

    protected void invokeClientConnectedEvent(SocketClient<?> client) {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onClientConnected(this, new SocketClientInfoEventArgs(client));
            }
        }
    }

    protected void invokeClientDisConnectedEvent(SocketClient<?> client) {
        for (ISocketServerEvent item : this.listener) {
            if (item != null) {
                item.onClientDisconnected(this, new SocketClientInfoEventArgs(client));
            }
        }
    }
    //endregion
}
