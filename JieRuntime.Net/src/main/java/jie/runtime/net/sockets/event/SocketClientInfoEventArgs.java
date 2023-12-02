package jie.runtime.net.sockets.event;

import jie.runtime.net.sockets.SocketClient;

/**
 * 表示包含套接字客户端信息事件数据的类
 *
 * @author jiegg
 */
public class SocketClientInfoEventArgs extends SocketEventArgs {

    //region --字段--
    private final SocketClient<?> client;
    //endregion

    //region --属性--

    /**
     * 获取当前事件的客户端
     */
    public SocketClient<?> getClient() {
        return client;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketClientInfoEventArgs} 类的新实例
     *
     * @param client 套接字客户端
     */
    public SocketClientInfoEventArgs(SocketClient<?> client) {
        this.client = client;
    }
    //endregion
}
