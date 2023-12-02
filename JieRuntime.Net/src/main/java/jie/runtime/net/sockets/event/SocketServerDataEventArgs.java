package jie.runtime.net.sockets.event;

import jie.runtime.net.sockets.SocketClient;

import java.net.InetSocketAddress;

/**
 * 表示服务端套接字数据事件数据的类
 *
 * @author jiegg
 */
public class SocketServerDataEventArgs extends SocketDataEventArgs {

    //region --字段--
    private final SocketClient<?> client;
    private final InetSocketAddress remoteAddr;
    //endregion

    //region --属性--

    /**
     * 获取当前事件的客户端
     */
    public SocketClient<?> getClient() {
        return client;
    }

    /**
     * 获取事件的远端地址
     */
    public InetSocketAddress getRemoteAddr() {
        return remoteAddr;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketDataEventArgs} 类的新实例
     *
     * @param data 相关的数据
     */
    public SocketServerDataEventArgs(SocketClient<?> client, InetSocketAddress remoteAddr, byte[] data) {
        super(data);
        this.client = client;
        this.remoteAddr = remoteAddr;
    }
    //endregion
}
