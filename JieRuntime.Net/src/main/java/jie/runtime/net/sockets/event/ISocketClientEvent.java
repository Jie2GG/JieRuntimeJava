package jie.runtime.net.sockets.event;

/**
 * 提供套接字客户端事件监听服务的接口
 *
 * @author jiegg
 */
public interface ISocketClientEvent {

    /**
     * 表示客户端成功连接到远程服务器的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onConnected(Object sender, SocketEventArgs args);

    /**
     * 表示客户端断开与远程服务器连接的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onDisconnected(Object sender, SocketEventArgs args);

    /**
     * 表示客户端收到远程服务器数据的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   数据事件参数
     */
    void onReceiveData(Object sender, SocketDataEventArgs args);

    /**
     * 表示客户端发送数据到远程服务器的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   数据事件参数
     */
    void onSendData(Object sender, SocketDataEventArgs args);

    /**
     * 表示套接字客户端出现异常的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   异常事件参数
     */
    void onException(Object sender, SocketExceptionEventArgs args);
}
