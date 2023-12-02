package jie.runtime.rpc.proxy;

import java.lang.reflect.Method;

/**
 * 表示代理调用的接口
 *
 * @author jiegg
 */
public interface IProxyInvoke {

    /**
     * 每当调用代理类型上的任何方法时，都会调用此方法
     *
     * @param targetMethod 调用者调用的方法
     * @param args         调用者传递给方法的参数
     * @return 返回给调用者的对象，void 方法将返回 {@code null}
     */
    Object invokeMethod(Method targetMethod, Object[] args) throws Throwable;
}
