package jie.runtime.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 表示远程调用服务提供 Java 语言的类型转换的服务类
 *
 * @author jiegg
 */
class RpcJavaTypeConverter extends RpcTypeConverter {

    //region --字段--
    private static final Map<Class<?>, String> TYPE_MAP;
    //endregion

    //region --属性--

    /**
     * 获取类型字典
     */
    @Override
    protected Map<Class<?>, String> getTypeMap() {
        return TYPE_MAP;
    }
    //endregion

    //region --构造函数--
    static {
        TYPE_MAP = new HashMap<>();
        // 字节
        TYPE_MAP.put(byte.class, byte.class.getSimpleName());
        TYPE_MAP.put(Byte.class, byte.class.getSimpleName());
        // 字符
        TYPE_MAP.put(char.class, char.class.getSimpleName());
        TYPE_MAP.put(Character.class, char.class.getSimpleName());
        // 16位整数
        TYPE_MAP.put(short.class, short.class.getSimpleName());
        TYPE_MAP.put(Short.class, short.class.getSimpleName());
        // 32位整数
        TYPE_MAP.put(int.class, int.class.getSimpleName());
        TYPE_MAP.put(Integer.class, int.class.getSimpleName());
        // 64位整数
        TYPE_MAP.put(long.class, long.class.getSimpleName());
        TYPE_MAP.put(Long.class, long.class.getSimpleName());
        // 单精度浮点
        TYPE_MAP.put(float.class, float.class.getSimpleName());
        TYPE_MAP.put(Float.class, float.class.getSimpleName());
        // 双精度浮点
        TYPE_MAP.put(double.class, double.class.getSimpleName());
        TYPE_MAP.put(Double.class, double.class.getSimpleName());
        // 布尔值
        TYPE_MAP.put(boolean.class, boolean.class.getSimpleName());
        TYPE_MAP.put(Boolean.class, boolean.class.getSimpleName());

    }
    //endregion
}
