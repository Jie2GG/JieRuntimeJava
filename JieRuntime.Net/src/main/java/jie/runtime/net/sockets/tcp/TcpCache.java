package jie.runtime.net.sockets.tcp;

import jie.runtime.BinaryConvert;
import jie.runtime.utils.ArrayUtils;

import java.util.Arrays;

/**
 * 提供 TCP 协议网络客户端使用的缓冲区
 *
 * @author jiegg
 */
class TcpCache {

    //region --字段--
    private final CacheList data;
    private byte packetHeaderLength;
    //endregion

    //region --属性--
    public void setPacketHeaderLength(byte packetHeaderLength) {
        this.packetHeaderLength = packetHeaderLength;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link TcpCache} 类的新实例
     */
    public TcpCache(byte packetHeaderLength) {
        this.packetHeaderLength = packetHeaderLength;
        this.data = new CacheList();
    }
    //endregion

    //region --公开方法--

    /**
     * 将数据推入缓冲区
     *
     * @param data 要推入的数据
     */
    public void push(byte[] data) {
        this.data.addAll(Arrays.asList(ArrayUtils.toObject(data)));
    }

    /**
     * 尝试拉取缓冲区中的完整数据包
     *
     * @return 如果缓冲区中的数据可以形成完整数据包, 则为 <code>true</code>, 否则为 <code>false</code>
     */
    public boolean isPull() {
        return this.getPacketLength() <= this.data.size();
    }

    /**
     * 拉取缓冲区中的完整数据包
     *
     * @return 拉取的完整数据包
     */
    public byte[] pull() {
        int packetLength = this.getPacketLength();
        if (packetLength >= 0) {
            // 读取数据
            byte[] src = ArrayUtils.toPrimitive(this.data.stream().limit(packetLength).toArray(Byte[]::new));

            // 拆分数据包
            byte[] dest = new byte[packetLength - this.packetHeaderLength];
            System.arraycopy(src, this.packetHeaderLength, dest, 0, dest.length);

            // 清理缓冲区
            this.data.removeRange(0, packetLength);

            return dest;
        }
        return null;
    }
    //endregion

    //region --私有方法--
    private int getPacketLength() {
        if (this.data.size() >= this.packetHeaderLength) {

            // 读取封包长度
            byte[] lenBytes = new byte[this.packetHeaderLength];
            for (int i = 0; i < lenBytes.length; i++) {
                lenBytes[i] = this.data.get(i);
            }
            return BinaryConvert.toInt32(lenBytes, true);
        }
        return -1;
    }
    //endregion
}
