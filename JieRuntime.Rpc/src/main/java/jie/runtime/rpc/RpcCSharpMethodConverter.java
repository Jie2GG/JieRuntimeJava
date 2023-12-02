package jie.runtime.rpc;

import java.lang.reflect.Method;

/**
 * 表示远程调用服务提供 C# 方法转换服务的类
 */
public class RpcCSharpMethodConverter extends RpcMethodConverter {

    /**
     * 将远程调用的指定方法根据规则进行比较
     *
     * @param method     要匹配的指定源方法
     * @param methodName 用于比较的方法名称
     * @return 如果指定的源方法和方法名称一致则为 <code>true</code>, 否则为 <code>false</code>
     */
    @Override
    public boolean isEquals(Method method, String methodName) {
        // TODO 这里补充 C# 语言的 getter setter 方法比较

        // 默认的方法比对方式
        return method.getName().equals(methodName);
    }
}
