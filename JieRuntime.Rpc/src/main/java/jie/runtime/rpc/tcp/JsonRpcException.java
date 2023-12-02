package jie.runtime.rpc.tcp;

import jie.runtime.rpc.RpcException;

/**
 * 表示 Json 远程调用服务指定服务期间发生的错误
 *
 * @author jiegg
 */
public class JsonRpcException extends RpcException {

    //region --构造函数--

    /**
     * 初始化 {@link RpcException} 类的新实例, 其详细消息为 {@code null}. 原因没有初始化, 并且可能随后通过调用 {@link #initCause(Throwable)} 初始化
     */
    public JsonRpcException() {
    }

    /**
     * 使用指定的详细信息消息初始化 {@link RpcException} 类的新实例. 原因没有初始化, 并且可能随后通过调用 {@link #initCause(Throwable)} 初始化
     *
     * @param message 详细信息, 详细信息将通过 {@link #getMessage()} 方法保存以供以后检索
     */
    public JsonRpcException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因初始化 {@link RpcException} 类的新实例
     * <p>请注意，与{@code cause}关联的详细消息是<i>而不是</i>，它会自动并入这个异常的详细消息中</p>
     *
     * @param message 详细信息, 详细信息将通过 {@link #getMessage()} 方法保存以供以后检索
     * @param cause   原因, 通过{@link #getCause()}方法保存以供以后检索 (允许<tt>null</tt>, 表示原因不存在或未知)
     */
    public JsonRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 初始化 {@link RpcException} 类的新实例, 带有指定的原因和一个详细消息
     * <tt>(cause==null ?null: cause. tostring ())</tt>
     * (通常包含<tt>cause</tt>的类和详细消息). 此构造函数对于这个异常非常有用, 这些异常只不过是其他可抛出对象的包装器.
     *
     * @param cause 原因, 通过{@link #getCause()}方法保存以供以后检索 (允许<tt>null</tt>, 表示原因不存在或未知)
     */
    public JsonRpcException(Throwable cause) {
        super(cause);
    }

    JsonRpcException(JsonRpcResponseError responseError) {
        super(responseError.getMessage(), responseError.getData() == null ? null : new JsonRpcException(responseError.getData()));
    }

    JsonRpcException(JsonRpcError error) {
        super(error.getMessage(), error.getInnerException() == null ? null : new JsonRpcException(error.getInnerException()));

        this.setCode(error.getCode());
        this.setStackTrace(error.getStackTrace());
        this.setSource(error.getSource());
    }
    //endregion
}
