package jie.runtime.net.sockets.event;

/**
 * 表示套接字数据事件数据的类
 *
 * @author jiegg
 */
public class SocketDataEventArgs extends SocketEventArgs {

    //region --字段--
    private final byte[] data;
    //endregion

    //region --属性--
    /**
     * 获取当前事件的数据
     */
    public byte[] getData() {
        return data;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketDataEventArgs} 类的新实例
     *
     * @param data 相关的数据
     */
    public SocketDataEventArgs(byte[] data) {
        this.data = data;
    }
    //endregion
}
