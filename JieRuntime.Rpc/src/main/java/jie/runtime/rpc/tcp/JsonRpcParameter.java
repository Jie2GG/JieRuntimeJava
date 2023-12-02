package jie.runtime.rpc.tcp;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 表示 Json 远程调用参数的类
 *
 * @author jiegg
 */
class JsonRpcParameter implements Serializable {

    //region --字段--
    @JSONField(name = "type")
    private String type;
    private Object value;
    //endregion

    //region --属性--

    /**
     * 获取参数的类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置参数的类型
     *
     * @param value 类型字符串
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取参数的值
     */
    @JSONField(name = "value")
    public Object getValue() {
        return value;
    }

    /**
     * 设置参数的值
     *
     * @param value 参数值
     */
    @JSONField(serialize = false)
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 设置参数的值
     *
     * @param value 参数的值
     */
    @JSONField(name = "value")
    public void setValue(String value) {
        this.value = value;
    }
    //endregion
}
