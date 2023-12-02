package jie.runtime.rpc.tcp;

/**
 * 表示远程调用封包类型的枚举
 *
 * @author jiegg
 */
enum PacketType {

    /**
     * 表示远程调用封包类型是请求
     */
    REQUEST((byte) 0x10),
    /**
     * 表示远程调用风暴类型是响应
     */
    RESPONSE((byte) 0x20);

    //region --字段--
    private final byte value;
    //endregion

    //region --属性--

    /**
     * 获取当前类型枚举的值
     */
    public byte getValue() {
        return value;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link PacketType} 类的新实例
     *
     * @param value 枚举类型的值
     */
    PacketType(byte value) {
        this.value = value;
    }
    //endregion
}
