package jie.runtime.rpc.tcp;

public class JsonRpcNetworkException extends JsonRpcException {

    /**
     * 初始化 {@link JsonRpcNetworkException} 类的新实例, 其详细消息为 {@code null}. 原因没有初始化, 并且可能随后通过调用 {@link #initCause(Throwable)} 初始化
     */
    public JsonRpcNetworkException() {
        super("数据接受失败, 网络环境可能存在异常");
        super.setCode(-32300);
    }
}
