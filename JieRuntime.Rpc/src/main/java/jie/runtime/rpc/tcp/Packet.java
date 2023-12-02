package jie.runtime.rpc.tcp;

import java.io.Serializable;

/**
 * 表示远程调用封包的类
 *
 * @author jiegg
 */
class Packet implements Serializable {

    //region --字段--
    private final PacketType type;
    private final long tag;
    private final byte[] data;
    //endregion

    //region --属性--

    /**
     * 获取封包类型
     */
    public PacketType getType() {
        return type;
    }

    /**
     * 获取封包标识
     */
    public long getTag() {
        return tag;
    }

    /**
     * 获取封包数据
     */
    public byte[] getData() {
        return data;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link Packet} 类的新实例
     *
     * @param type 远程调用封包类型
     * @param tag  封包标识
     * @param data 封包数据
     */
    public Packet(PacketType type, long tag, byte[] data) {
        this.type = type;
        this.tag = tag;
        this.data = data;
    }
    //endregion
}
