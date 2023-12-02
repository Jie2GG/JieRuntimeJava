package jie.runtime.net.sockets.event;


/**
 * 表示套接字异常事件数据的类
 *
 * @author jiegg
 */
public class SocketExceptionEventArgs extends SocketEventArgs {

    //region --字段--
    private final Throwable exception;
    //endregion

    //region --属性--

    /**
     * 获取当前事件的异常
     */
    public Throwable getException() {
        return exception;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link SocketExceptionEventArgs} 类的新实例
     *
     * @param exception 异常信息
     */
    public SocketExceptionEventArgs(Throwable exception) {
        this.exception = exception;
    }
    //endregion
}
