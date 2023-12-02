package jie.runtime.net.sockets.event;

/**
 * 提供套接字服务端事件监听服务的接口
 *
 * @author jiegg
 */
public interface ISocketServerEvent {

    /**
     * 表示服务端启动的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onStarted(Object sender, SocketEventArgs args);

    /**
     * 表示服务端停止的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onStopped(Object sender, SocketEventArgs args);

    /**
     * 表示服务端收到数据的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onReceiveData(Object sender, SocketServerDataEventArgs args);

    /**
     * 表示服务端发送数据的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onSendData(Object sender, SocketServerDataEventArgs args);

    /**
     * 表示服务端异常的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onException(Object sender, SocketExceptionEventArgs args);

    /**
     * 表示有客户端连接到服务端的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onClientConnected(Object sender, SocketClientInfoEventArgs args);

    /**
     * 表示客户端断开连接服务端的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onClientDisconnected(Object sender, SocketClientInfoEventArgs args);
}
