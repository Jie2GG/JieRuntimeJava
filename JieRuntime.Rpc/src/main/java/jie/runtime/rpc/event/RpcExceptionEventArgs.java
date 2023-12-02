package jie.runtime.rpc.event;

/**
 * 表示包哈远程调用服务异常事件数据的类
 *
 * @author jiegg
 */
public class RpcExceptionEventArgs extends RpcEventArgs {

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
     * 初始化 {@link RpcExceptionEventArgs} 类的新实例
     *
     * @param exception 异常信息
     */
    public RpcExceptionEventArgs(Throwable exception) {
        this.exception = exception;
    }
    //endregion
}
