package jie.runtime.rpc;

/**
 * 表示远程调用服务提供 Java 方法转换服务的类
 */
class RpcJavaMethodConverter extends RpcMethodConverter {

    /**
     * 将远程调用指定的方法根据规则转换为本地可识别的方法
     *
     * @param methodName 方法名称
     * @return 被转换的方法名称
     */
    @Override
    public String convert(String methodName) {
        // Java -> Java 方法不需要转换
        return methodName;
    }
}
