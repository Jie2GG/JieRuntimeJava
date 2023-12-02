package jie.runtime.rpc.proxy;

import com.sun.org.apache.bcel.internal.generic.ClassGenException;
import net.sf.cglib.proxy.Enhancer;

/**
 * 提供代理的基础类, 该类的是抽象的
 *
 * @author jiegg
 */
public abstract class ProxyBase implements IProxyInvoke {

    //region --字段--
    private final Enhancer enhancer;
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link ProxyBase} 类的新实例
     */
    public ProxyBase() {
        this.enhancer = new Enhancer();
    }
    //endregion

    //region --公开方法--

    /**
     * 解析指定的接口并返回该接口的动态代理类实例
     *
     * @param tClass 被代理的类型
     * @param <T>    被代理的类型
     * @return 实现该接口的动态代理类
     */
    @SuppressWarnings("unchecked")
    public <T> T resolver(Class<T> tClass) {
        if (tClass == null) {
            throw new NullPointerException("参数: tClass 是 null");
        }
        if (!tClass.isInterface()) {
            throw new ClassGenException("参数: tClass 不是接口类型");
        }

        this.enhancer.setSuperclass(tClass);
        this.enhancer.setCallback(new DispatchProxyInvoke(this));
        return (T) this.enhancer.create();
    }
    //endregion
}
