package jie.runtime.io;

import jie.runtime.BinaryConvert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 提供缓冲区写入服务的类
 *
 * @author jiegg
 */
public class BufferWriter extends ByteArrayOutputStream {

    //region --公开方法--

    /**
     * 将 {@link Byte} 写入流的指定位置
     *
     * @param value 要写入的 {@link Byte} 值
     */
    public void write(byte value) {
        super.write(value);
    }

    /**
     * 将 {@link Boolean} 写入流的指定位置
     *
     * @param value 要写入的 {@link Boolean} 值
     */
    public void write(boolean value) {
        this.write((byte) (value ? 1 : 0));
    }

    /**
     * 将 {@link Character} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Character} 值
     * @throws IOException I/O错误
     */
    public void write(char value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Character} 写入流的指定位置
     *
     * @param value       要写入的 {@link Character} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(char value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    /**
     * 将字符数组以大端序写入流的指定位置
     *
     * @param chars 要写入的字符数组
     * @throws IOException I/O错误
     */
    public void write(char[] chars) throws IOException {
        this.write(chars, true);
    }

    /**
     * 将字符数组写入流的指定位置
     *
     * @param chars       要写入的字符数组
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(char[] chars, boolean isBigEndian) throws IOException {
        if (chars == null) {
            throw new NullPointerException("参数: chars 是 null");
        }
        for (char c : chars) {
            this.write(c, isBigEndian);
        }
    }

    /**
     * 将 {@link Short} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Short} 值
     * @throws IOException I/O错误
     */
    public void write(short value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Short} 写入流的指定位置
     *
     * @param value       要写入的 {@link Short} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(short value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    /**
     * 将 {@link Integer} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Integer} 值
     * @throws IOException I/O错误
     */
    public void write(Integer value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Integer} 写入流的指定位置
     *
     * @param value       要写入的 {@link Integer} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(int value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    /**
     * 将 {@link Long} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Long} 值
     * @throws IOException I/O错误
     */
    public void write(long value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Long} 写入流的指定位置
     *
     * @param value       要写入的 {@link Long} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(long value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    /**
     * 将 {@link Float} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Float} 值
     * @throws IOException I/O错误
     */
    public void write(float value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Float} 写入流的指定位置
     *
     * @param value       要写入的 {@link Float} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(float value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    /**
     * 将 {@link Double} 以大端序写入流的指定位置
     *
     * @param value 要写入的 {@link Double} 值
     * @throws IOException I/O错误
     */
    public void write(double value) throws IOException {
        this.write(value, true);
    }

    /**
     * 将 {@link Double} 写入流的指定位置
     *
     * @param value       要写入的 {@link Double} 值
     * @param isBigEndian 是否以大端序模式写入
     * @throws IOException I/O错误
     */
    public void write(double value, boolean isBigEndian) throws IOException {
        this.write(BinaryConvert.getBytes(value, isBigEndian));
    }

    //endregion
}
