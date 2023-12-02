package jie.runtime.rpc;

/**
 * 表示远程调用服务实例的类
 *
 * @author jiegg
 */
public class RpcService {

    //region --字段--
    private final Class<?> type;
    private final Object instance;
    //endregion

    //region --属性--

    /**
     * 获取远程调用服务实例的接口类型
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * 获取远程调用服务实例的具体执行实例
     */
    public Object getInstance() {
        return instance;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link RpcService} 类的新实例
     *
     * @param type     指定接口类型
     * @param instance 指定接口类型对应的实例
     */
    public RpcService(Class<?> type, Object instance) {
        this.type = type;
        this.instance = instance;
    }
    //endregion
}
