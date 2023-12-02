package jie.runtime.rpc.tcp;

import jie.runtime.rpc.RpcException;

/**
 * 表示 Json 远程调用服务指定服务期间发生的错误
 *
 * @author jiegg
 */
public class JsonRpcException extends RpcException {

    //region --构造函数--
    JsonRpcException(JsonRpcResponseError responseError) {
        super(responseError.getMessage(), responseError.getData() == null ? null : new JsonRpcException(responseError.getData()));
    }

    JsonRpcException(JsonRpcError error) {
        super(error.getMessage(), error.getInnerException() == null ? null : new JsonRpcException(error.getInnerException()));

        this.setStackTrace(error.getStackTrace());
        this.setSource(error.getSource());
    }
    //endregion
}
