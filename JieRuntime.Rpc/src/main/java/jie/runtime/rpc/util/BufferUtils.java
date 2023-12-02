package jie.runtime.rpc.util;

import jie.runtime.io.BufferReader;
import jie.runtime.io.BufferWriter;

import java.io.IOException;

/**
 * {@link BufferWriter} 工具
 *
 * @author jiegg
 */
public class BufferUtils {

    /**
     * 从流中指定位置开始, 读取短令牌数据
     *
     * @param buffer 要读取的流
     * @return 一个字节数组, 包含读取的段令牌数据
     * @throws NullPointerException 参数 buffer 是 null
     */
    public static byte[] readShortToken(BufferReader buffer) {
        if (buffer == null) {
            throw new NullPointerException("参数: buffer 是 null");
        }

        short len = buffer.readInt16();
        return buffer.readBytes(len);
    }

    /**
     * 将数据以短令牌的形式写入流指定的位置
     *
     * @param buffer 要写入的流
     * @param data   令牌数据
     * @throws IOException I/O错误
     * @throws NullPointerException 参数 buffer 或 data 是 null
     */
    public static void writeShortToken(BufferWriter buffer, byte[] data) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("参数: buffer 是 null");
        }

        if (data == null) {
            throw new NullPointerException("参数: data 是 null");
        }

        buffer.write((short) data.length);
        buffer.write(data);
    }
}
