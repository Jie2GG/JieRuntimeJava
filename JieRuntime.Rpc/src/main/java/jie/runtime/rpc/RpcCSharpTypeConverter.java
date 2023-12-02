package jie.runtime.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 表示远程调用服务提供 C# 语言的类型转换的服务类
 *
 * @author jiegg
 */
class RpcCSharpTypeConverter extends RpcTypeConverter {

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
        TYPE_MAP.put(byte.class, "Byte");
        TYPE_MAP.put(Byte.class, "Byte");
        // 字符
        TYPE_MAP.put(char.class, "Char");
        TYPE_MAP.put(Character.class, "Char");
        // 16位整数
        TYPE_MAP.put(short.class, "Int16");
        TYPE_MAP.put(Short.class, "Int16");
        // 32位整数
        TYPE_MAP.put(int.class, "Int32");
        TYPE_MAP.put(Integer.class, "Int32");
        // 64位整数
        TYPE_MAP.put(long.class, "Int64");
        TYPE_MAP.put(Long.class, "Int64");
        // 单精度浮点
        TYPE_MAP.put(float.class, "Single");
        TYPE_MAP.put(Float.class, "Single");
        // 双精度浮点
        TYPE_MAP.put(double.class, "Double");
        TYPE_MAP.put(Double.class, "Double");
        // 布尔值
        TYPE_MAP.put(boolean.class, "Boolean");
        TYPE_MAP.put(Boolean.class, "Boolean");
    }
    //endregion
}
