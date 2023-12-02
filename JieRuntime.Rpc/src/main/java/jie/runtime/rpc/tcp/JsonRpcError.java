package jie.runtime.rpc.tcp;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 表示 Json 远程调用异常的类
 *
 * @author jiegg
 */
class JsonRpcError implements Serializable {

    //region --字段--
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "source")
    private String source;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "stack_trace")
    private String stackTrace;
    @JSONField(name = "inner_exception")
    private JsonRpcError innerException;
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
     * @param value 错误代码的值
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * 获取引起错误的应用程序或对象的名称
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置引起错误的应用程序或对象的名称
     *
     * @param value 应用程序或对象的名称
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * 获取描述当前错误的信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置描述当前错误的信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取调用堆栈上即时帧的字符串表示形式
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * 设置调用堆栈上即时帧的字符串表示形式
     *
     * @param stackTrace 堆栈字符串
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * 获取导致当前错误的 {@link JsonRpcError} 实例
     */
    public JsonRpcError getInnerException() {
        return innerException;
    }

    /**
     * 设置导致当前错误的 {@link JsonRpcError} 实例
     *
     * @param value 导致当前错误的 {@link JsonRpcError} 实例
     */
    public void setInnerException(JsonRpcError value) {
        this.innerException = value;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link JsonRpcError 类的新实例}
     *
     * @param throwable 异常信息
     */
    public JsonRpcError(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("参数: throwable 是 null");
        }

        // 来源就是导致异常的类名
        this.source = throwable.getClass().getName();
        // 处理异常消息
        this.message = throwable.getMessage();

        // 处理堆栈
        StackTraceElement[] trace = throwable.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        for (StackTraceElement traceElement : trace) {
            traceBuilder.append("\tat").append(traceElement.toString()).append("\n");
        }
        this.stackTrace = traceBuilder.toString();

        // 处理嵌套异常
        if (throwable.getCause() != null) {
            this.innerException = new JsonRpcError(throwable.getCause());
        }
    }
    //endregion
}
