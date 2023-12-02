package jie.runtime.io;

import jie.runtime.BinaryConvert;
import jie.runtime.utils.ArrayUtils;

import java.io.ByteArrayInputStream;

/**
 * 提供缓冲区读取服务的类
 *
 * @author jiegg
 */
public class BufferReader extends ByteArrayInputStream {

    //region --属性--

    /**
     * 获取当前流中的位置
     */
    public int getPosition() {
        return super.pos;
    }

    /**
     * 设置当前流中的位置
     *
     * @param value 流中的位置
     */
    public void setPosition(int value) {
        super.pos = value;
    }

    /**
     * 获取当前流的长度
     */
    public int size() {
        return super.count;
    }

    /**
     * 获取当前缓冲区剩余的数据长度
     */
    public int overSize() {
        return this.size() - this.getPosition();
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link BufferReader} 类的新实例
     *
     * @param buf 输入的字节数组
     */
    public BufferReader(byte[] buf) {
        super(buf);
    }
    //endregion

    /**
     * 从流中指定位置开始, 读取流中的所有字节
     *
     * @return 一个新的字节数组, 包含流中剩余数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public byte[] readAll() {
        return this.readBytes(this.overSize());
    }

    /**
     * 从流中指定位置开始, 读取1字节长度的数据
     *
     * @return 一个 {@link Byte} 值
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public byte readByte() {
        int read = this.read();
        if (read == -1) {
            throw new IndexOutOfBoundsException("无法继续读取数据, 因为已经读取到流的末尾");
        }
        return (byte) read;
    }

    /**
     * 从流中指定位置开始, 读取指定长度的数据
     *
     * @param count 读取数据的长度
     * @return 一个字节数组, 包含已读取的指定长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public byte[] readBytes(int count) {
        byte[] buf = new byte[count];
        if (this.read(buf, 0, buf.length) != buf.length) {
            throw new IndexOutOfBoundsException("无法继续读取数据, 因为已经读取到流的末尾");
        }
        return buf;
    }

    /**
     * 从流中指定位置开始, 读取1字节长度的数据, 并转换为 {@link Boolean} 值
     *
     * @return 一个 {@link Boolean} 值, 如果读取到的值不是0则为 <code>true</code>, 否则为 <code>false</code>
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public boolean readBoolean() {
        return BinaryConvert.toBoolean(this.readBytes(Byte.BYTES), 0);
    }

    /**
     * 从流中指定位置开始, 以大端序读取2字节长度的数据, 并转换为 Unicode 字符
     *
     * @return 一个 Unicode 字符, 等效于已读取的2个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public char readChar() {
        return this.readChar(true);
    }

    /**
     * 从流中指定位置开始, 读取2字节长度的数据, 并转换为 Unicode 字符
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 Unicode 字符, 等效于已读取的2个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public char readChar(boolean isBigEndian) {
        return BinaryConvert.toChar(this.readBytes(Character.BYTES), 0, isBigEndian);
    }

    /**
     * 从当前流中以大端序读取指定数量的字符，返回字符数组中的数据
     *
     * @return 一个 Unicode 字符数组, 包含已读取的所有 Unicode 字符
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public char[] readChars(int count) {
        return this.readChars(count, true);
    }

    /**
     * 从当前流中读取指定数量的字符，返回字符数组中的数据
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 Unicode 字符数组, 包含已读取的所有 Unicode 字符
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public char[] readChars(int count, boolean isBigEndian) {
        char[] chars = new char[count];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = this.readChar(isBigEndian);
        }
        return chars;
    }

    /**
     * 从流中指定位置开始, 以大端序读取2字节长度的数据, 并转换为 {@link Short} 值
     *
     * @return 一个 {@link Short} 字符, 等效于已读取的2个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public short readInt16() {
        return this.readInt16(true);
    }

    /**
     * 从流中指定位置开始, 读取2字节长度的数据, 并转换为 {@link Short} 值
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 {@link Short} 字符, 等效于已读取的2个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public short readInt16(boolean isBigEndian) {
        return BinaryConvert.toInt16(this.readBytes(Short.BYTES), 0, isBigEndian);
    }

    /**
     * 从流中指定位置开始, 以大端序读取4字节长度的数据, 并转换为 {@link Integer} 值
     *
     * @return 一个 {@link Integer} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public int readInt32() {
        return this.readInt32(true);
    }

    /**
     * 从流中指定位置开始, 读取4字节长度的数据, 并转换为 {@link Integer} 值
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 {@link Integer} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public int readInt32(boolean isBigEndian) {
        return BinaryConvert.toInt32(this.readBytes(Integer.BYTES), 0, isBigEndian);
    }

    /**
     * 从流中指定位置开始, 以大端序读取4字节长度的数据, 并转换为 {@link Long} 值
     *
     * @return 一个 {@link Long} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public long readInt64() {
        return this.readInt64(true);
    }

    /**
     * 从流中指定位置开始, 读取4字节长度的数据, 并转换为 {@link Long} 值
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 {@link Long} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public long readInt64(boolean isBigEndian) {
        return BinaryConvert.toInt64(this.readBytes(Long.BYTES), 0, isBigEndian);
    }

    /**
     * 从流中指定位置开始, 以大端序读取4字节长度的数据, 并转换为 {@link Float} 值
     *
     * @return 一个 {@link Float} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public float readSingle() {
        return this.readSingle(true);
    }

    /**
     * 从流中指定位置开始, 读取4字节长度的数据, 并转换为 {@link Float} 值
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 {@link Float} 字符, 等效于已读取的4个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public float readSingle(boolean isBigEndian) {
        return BinaryConvert.toSingle(this.readBytes(Float.BYTES), 0, isBigEndian);
    }

    /**
     * 从流中指定位置开始, 以大端序读取8字节长度的数据, 并转换为 {@link Double} 值
     *
     * @return 一个 {@link Double} 字符, 等效于已读取的8个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public double readDouble() {
        return this.readDouble(true);
    }

    /**
     * 从流中指定位置开始, 读取8字节长度的数据, 并转换为 {@link Double} 值
     *
     * @param isBigEndian 是否以大端序模式读取
     * @return 一个 {@link Double} 字符, 等效于已读取的8个字节长度的数据
     * @throws IndexOutOfBoundsException 在读取所有字节之前达到流末尾
     */
    public double readDouble(boolean isBigEndian) {
        return BinaryConvert.toDouble(this.readBytes(Double.BYTES), 0, isBigEndian);
    }

    /**
     * 将一个字节数组数据并入流的末尾
     *
     * @param data 并入的字节数组
     */
    public void putData(byte[] data) {
        this.buf = ArrayUtils.concat(this.buf, data);
        this.count = this.buf.length;
    }

    /**
     * 将当前位置向前移动指定的长度
     *
     * @param len 移动的长度
     */
    public void rollback(int len) {
        if (len > this.getPosition()) {
            throw new IndexOutOfBoundsException("回退的长度超过了流已读的长度. len: " + len);
        }
        this.setPosition(this.getPosition() - len);
    }
}
