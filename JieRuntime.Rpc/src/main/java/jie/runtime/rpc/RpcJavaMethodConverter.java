package jie.runtime.rpc;

import java.lang.reflect.Method;

/**
 * 表示远程调用服务提供 Java 方法转换服务的类
 */
class RpcJavaMethodConverter extends RpcMethodConverter {

    /**
     * 将远程调用的指定方法根据规则进行比较
     *
     * @param method     要匹配的指定源方法
     * @param methodName 用于比较的方法名称
     * @return 如果指定的源方法和方法名称一致则为 <code>true</code>, 否则为 <code>false</code>
     */
    @Override
    public boolean isEquals(Method method, String methodName) {
        return method.getName().equals(methodName);
    }
}
