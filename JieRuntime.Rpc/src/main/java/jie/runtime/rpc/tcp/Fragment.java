package jie.runtime.rpc.tcp;

import jie.runtime.io.BufferReader;
import jie.runtime.io.BufferWriter;
import jie.runtime.rpc.util.BufferUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * 表示分片结构的类
 *
 * @author jiegg
 */
class Fragment implements Serializable {

    //region --字段--
    private final byte[] tag;
    private final int index;
    private final int count;
    private final byte[] data;
    //endregion

    //region --属性--

    /**
     * 获取分片标识
     */
    public byte[] getTag() {
        return tag;
    }

    /**
     * 获取分片索引
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取分片数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 获取分片数据
     */
    public byte[] getData() {
        return data;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link Fragment} 类的新实例
     *
     * @param tag   分片标识
     * @param index 分片索引
     * @param count 分片数量
     * @param data  分片数据
     */
    public Fragment(byte[] tag, int index, int count, byte[] data) {
        this.tag = tag;
        this.index = index;
        this.count = count;
        this.data = data;
    }
    //endregion

    //region --公开方法--

    /**
     * 将当前消息分片转换成字节数组的形式
     *
     * @return 一个字节数组, 包含当前实例的数据
     * @throws IOException I/O错误
     */
    public byte[] getBytes() throws IOException {
        BufferWriter writer = new BufferWriter();
        BufferUtils.writeShortToken(writer, this.tag);
        writer.write(this.index);
        writer.write(this.count);
        BufferUtils.writeShortToken(writer, this.data);
        return writer.toByteArray();
    }

    /**
     * 尝试将一个字节数组以消息分片的形式解析
     *
     * @param data 一个字节数组, 作为尝试解析的数据
     * @return 如果解析成功返回 {@link Fragment} 类的新实例, 否则返回 <code>null</code>
     */
    public static Fragment tryParse(byte[] data) {
        if (data != null) {
            try {
                BufferReader reader = new BufferReader(data);
                byte[] tag = BufferUtils.readShortToken(reader);
                short index = reader.readInt16();
                short count = reader.readInt16();
                byte[] readData = BufferUtils.readShortToken(reader);
                return new Fragment(tag, index, count, readData);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
    //endregion
}
