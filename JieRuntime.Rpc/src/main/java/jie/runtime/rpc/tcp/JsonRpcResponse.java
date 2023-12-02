package jie.runtime.rpc.tcp;

import com.alibaba.fastjson.annotation.JSONField;
import jie.runtime.rpc.RpcClientType;

import java.io.Serializable;

/**
 * 表示 Json 远程调用响应的类
 *
 * @author jiegg
 */
public class JsonRpcResponse implements Serializable {

    //region --字段--
    @JSONField(name = "ver")
    private String version = "3";
    @JSONField(name = "client")
    private RpcClientType clientType;
    @JSONField(name = "result")
    private Object result;
    @JSONField(name = "params")
    private JsonRpcParameter[] parameters;
    @JSONField(name = "error")
    private JsonRpcResponseError error;
    //endregion

    //region --属性--

    /**
     * 获取版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param value 版本号值
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * 获取客户端类型
     */
    public RpcClientType getClientType() {
        return clientType;
    }

    /**
     * 设置客户端类型
     *
     * @param value 客户端类型
     */
    public void setClientType(RpcClientType value) {
        this.clientType = value;
    }

    /**
     * 获取响应的执行返回值
     */
    public Object getResult() {
        return result;
    }

    /**
     * 设置响应的执行返回值
     *
     * @param value 返回值
     */
    public void setResult(Object value) {
        this.result = value;
    }

    /**
     * 获取回传参数
     */
    public JsonRpcParameter[] getParameters() {
        return parameters;
    }

    /**
     * 设置回传参数
     *
     * @param value 回传参数
     */
    public void setParameters(JsonRpcParameter[] value) {
        this.parameters = value;
    }

    /**
     * 获取服务的错误信息
     */
    public JsonRpcResponseError getError() {
        return error;
    }

    /**
     * 设置服务的错误信息
     *
     * @param value 错误信息
     */
    public void setError(JsonRpcResponseError value) {
        this.error = value;
    }
    //endregion

    //region --公开方法--

    /**
     * 根据错误创建返回值
     *
     * @param error 远程调用错误信息
     * @return 一个新的 {@link JsonRpcResponseError} 类的实例, 包含远程调用错误信息
     */
    public static JsonRpcResponse createError(JsonRpcResponseError error) {
        JsonRpcResponse response = new JsonRpcResponse();
        response.setError(error);
        return response;
    }
    //endregion
}
