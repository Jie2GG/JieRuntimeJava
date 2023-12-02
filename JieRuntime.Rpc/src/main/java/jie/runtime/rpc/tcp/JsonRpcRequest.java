package jie.runtime.rpc.tcp;

import com.alibaba.fastjson.annotation.JSONField;
import jie.runtime.rpc.RpcClientType;

import java.io.Serializable;

/**
 * 表示 Json 远程调用请求的类
 *
 * @author jiegg
 */
class JsonRpcRequest implements Serializable {

    //region --字段--
    @JSONField(name = "ver")
    private String version = "3";
    @JSONField(name = "client")
    private RpcClientType clientType;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "method")
    private String method;
    @JSONField(name = "params")
    private JsonRpcParameter[] parameters;
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
     * @param value 版本号
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
     * 获取请求执行的类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置请求执行的类型
     *
     * @param value 类型字符串
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取请求执行的方法
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置请求执行的方法
     *
     * @param value 方法字符串
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * 获取请求所需的参数
     */
    public JsonRpcParameter[] getParameters() {
        return parameters;
    }

    /**
     * 设置请求所需的参数
     *
     * @param value 请求参数
     */
    public void setParameters(JsonRpcParameter[] value) {
        this.parameters = value;
    }
    //endregion
}
