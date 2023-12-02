package jie.runtime.rpc.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 表示代理调用类
 *
 * @author jiegg
 */
public class DispatchProxyInvoke implements MethodInterceptor {

    //region --字段--
    private final IProxyInvoke targetProxyInvoke;
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link DispatchProxyInvoke} 类的新实例
     *
     * @param proxyInvoke 代理调用实例
     */
    public DispatchProxyInvoke(IProxyInvoke proxyInvoke) {
        this.targetProxyInvoke = proxyInvoke;
    }
    //endregion

    //region --公开方法--

    /**
     * 每当调用生成的代理类型上的任何方法时, 都会调用此方法
     *
     * @param obj    当前增强对象
     * @param method 调用者调用的方法
     * @param args   调用传递给方法的参数
     * @param proxy  代理的方法
     * @return 返回给调用者的对象, void 方法将返回 {@code null}
     * @throws Throwable 调用方法可能会发生异常
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (this.targetProxyInvoke != null) {
            this.targetProxyInvoke.invokeMethod(method, args);
        }
        return null;
    }
    //endregion
}
