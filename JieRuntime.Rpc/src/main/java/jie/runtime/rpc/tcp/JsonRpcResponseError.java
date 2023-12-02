package jie.runtime.rpc.tcp;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 表示 Json 远程调用错误信息的类
 *
 * @author jiegg
 */
class JsonRpcResponseError {

    //region --字段--
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "data")
    private JsonRpcError data;
    //endregion

    //region --属性--

    /**
     * 获取错误代码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置错误代码
     *
     * @param value 错误代码
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * 获取错误信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置错误信息
     *
     * @param value 错误信息
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取导致当前错误的异常信息
     */
    public JsonRpcError getData() {
        return data;
    }

    /**
     * 设置导致当前错误的异常信息
     *
     * @param value 异常信息
     */
    public void setData(JsonRpcError value) {
        this.data = value;
    }

    //endregion

    //region --公开方法--

    /**
     * 创建 JsonRpc 应用程序错误 (-32500)
     *
     * @param message 错误信息
     * @param e       引发错误的异常
     * @return 包含指定错误的 {@link JsonRpcResponseError} 类的新实例
     */
    public static JsonRpcResponseError createApplicationError(String message, Throwable e) {
        JsonRpcResponseError error = new JsonRpcResponseError();
        error.setCode(-32500);
        error.setMessage(message);
        error.setData(new JsonRpcError(e));
        return error;
    }

    /**
     * 创建 JsonRpc 格式解析错误 (-32700)
     *
     * @param e Json 格式解析错误的异常
     * @return 包含指定错误的 {@link JsonRpcResponseError} 类的新实例
     */
    public static JsonRpcResponseError createFormatterError(Throwable e) {
        JsonRpcResponseError error = new JsonRpcResponseError();
        error.setCode(-32700);
        error.setMessage(e.getMessage());
        return error;
    }

    /**
     * 创建 JsonRpc 系统错误 (-32400)
     *
     * @param message 错误信息
     * @param e       引发错误的异常
     * @return 包含指定错误的 {@link JsonRpcResponseError} 类的新实例
     */
    public static JsonRpcResponseError createSystemError(String message, Throwable e) {
        JsonRpcResponseError error = new JsonRpcResponseError();
        error.setCode(-32400);
        error.setMessage(message);
        error.setData(new JsonRpcError(e));
        return error;
    }

    /**
     * 创建 JsonRpc 服务类型或服务类型方法找不到错误 (-32601)
     *
     * @param type 引发此错误的类型名称
     * @return 包含指定错误的 {@link JsonRpcResponseError} 类的新实例
     */
    public static JsonRpcResponseError createTypeNotFoundError(String type) {
        JsonRpcResponseError error = new JsonRpcResponseError();
        error.setCode(-32601);
        error.setMessage(String.format("找不到与 “%s” 匹配的服务, 可能该服务未注册", type));
        return error;
    }

    /**
     * 创建 JsonRpc 服务类型或服务类型方法找不到错误 (-32601)
     *
     * @param type   引发此错误的类型名称
     * @param method 引发此错误的方法名称
     * @return 包含指定错误的 {@link JsonRpcResponseError} 类的新实例
     */
    public static JsonRpcResponseError createMethodNotFoundError(String type, String method) {
        JsonRpcResponseError error = new JsonRpcResponseError();
        error.setCode(-32601);
        error.setMessage(String.format("无法在 “%s” 服务中找到与 “%s” 匹配的方法", type, method));
        return error;
    }
    //endregion
}
