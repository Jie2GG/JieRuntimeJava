package jie.runtime.rpc;

import java.util.Map;

/**
 * 表示远程调用服务提供类型转换服务的接口
 *
 * @author jiegg
 */
public abstract class RpcTypeConverter {

    /**
     * 获取类型字典
     */
    protected abstract Map<Class<?>, String> getTypeMap();

    /**
     * 获取类型的名称
     *
     * @param type 一个 {@link Class}, 被获取名称
     * @return 类型名称
     */
    public String getTypeName(Class<?> type) {
        if (this.getTypeMap().containsKey(type)) {
            return this.getTypeMap().get(type);
        }
        return type.getSimpleName();
    }

    /**
     * 比较指定名称和类型是否一致
     *
     * @param type 比较的类型
     * @param name 被比较的名称
     * @return 如果类型可以被描述成指定名称则为 <cdoe>true</cdoe>, 否则为 <cdoe>false</cdoe>
     */
    public boolean isEquals(Class<?> type, String name) {
        if (this.getTypeMap().containsKey(type)) {
            return this.getTypeMap().get(type).equals(name);
        }
        return type.getSimpleName().equals(name);
    }
}
