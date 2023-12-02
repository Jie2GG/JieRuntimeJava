package jie.runtime.utils;

import java.lang.reflect.Array;

/**
 * 提供一组数组快速处理方法
 *
 * @author jiegg
 */
public class ArrayUtils {

    //region --常量--
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    //endregion

    /**
     * 将输入的数组按顺序依次拼接到第一个数组的尾部
     *
     * @param source 要连接的所有数组
     * @return 一个数组, 包含多个输入数组的连接元素
     */
    public static byte[] concat(byte[]... source) {
        if (source == null) {
            throw new NullPointerException("参数: source, 值不能为空");
        }

        if (source.length == 0) {
            return new byte[0];
        }

        int len = 0;
        for (byte[] bytes : source) {
            len += bytes.length;
        }
        byte[] newArray = new byte[len];
        len = 0;
        for (byte[] bytes : source) {
            System.arraycopy(bytes, 0, newArray, len, bytes.length);
        }
        return newArray;
    }

    /**
     * 将输入的数组按顺序依次拼接到第一个数组的尾部
     *
     * @param source 要连接的所有数组
     * @return 一个数组, 包含多个输入数组的连接元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(Class<T> tClass, T[]... source) {
        if (source == null) {
            throw new NullPointerException("参数: source, 值不能为空");
        }

        if (source.length == 0) {
            throw new IllegalArgumentException("参数: source, 合并的数组为空");
        }

        int len = 0;
        for (T[] bytes : source) {
            len += bytes.length;
        }
        T[] newArray = (T[]) Array.newInstance(tClass, len);
        len = 0;
        for (T[] bytes : source) {
            System.arraycopy(bytes, 0, newArray, len, bytes.length);
        }
        return newArray;
    }

    /**
     * 从输入的开始返回指定数量的连续元素
     *
     * @param source 返回元素的序列
     * @param count  要返回的元素数量
     * @return 一个数组, 包含从输入数组开始的指定数量的元素
     */
    public static byte[] left(byte[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source 是 null");
        }

        if (count < 0 || count > source.length) {
            throw new IllegalArgumentException("参数: count 超过了数组 source 的范围");
        }

        if (count == 0) {
            return new byte[0];
        }

        byte[] newArr = new byte[count];
        System.arraycopy(source, 0, newArr, 0, newArr.length);
        return newArr;
    }

    /**
     * 从输入的开始返回指定数量的连续元素
     *
     * @param source 返回元素的序列
     * @param count  要返回的元素数量
     * @return 一个数组, 包含从输入数组开始的指定数量的元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] left(Class<T> tClass, T[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source 是 null");
        }

        if (count < 0 || count > source.length) {
            throw new IllegalArgumentException("参数: count 超过了数组 source 的范围");
        }

        if (count == 0) {
            return (T[]) Array.newInstance(tClass, 0);
        }

        T[] newArr = (T[]) Array.newInstance(tClass, count);
        System.arraycopy(source, 0, newArr, 0, newArr.length);
        return newArr;
    }

    /**
     * 跳过数组中指定的元素数量, 然后返回剩余元素
     *
     * @param source 返回元素的序列
     * @param count  返回剩余元素前要跳过的元素数量
     * @return 一个数组, 其中包含输入数组中的指定数量后出现的元素
     */
    public static byte[] skip(byte[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source 是 null");
        }

        return right(source, source.length - count);
    }

    /**
     * 跳过数组中指定的元素数量, 然后返回剩余元素
     *
     * @param source 返回元素的序列
     * @param count  返回剩余元素前要跳过的元素数量
     * @return 一个数组, 其中包含输入数组中的指定数量后出现的元素
     */
    public static <T> T[] skip(Class<T> tClass, T[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source 是 null");
        }

        return right(tClass, source, source.length - count);
    }

    /**
     * 从数组的结尾返回指定数量的连续元素
     *
     * @param source 返回元素的字节序列
     * @param count  要返回的元素数量
     * @return 一个字节数组, 包含从输入数组结尾的指定数量的元素
     * @throws NullPointerException     source 是 null
     * @throws IllegalArgumentException count 不能是负数且不能大于 source.length
     */
    public static byte[] right(byte[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source, 值不能为空");
        }

        if (count < 0) {
            throw new IllegalArgumentException("参数: count, 值不能为负数");
        }

        if (count > source.length) {
            throw new IllegalArgumentException(String.format("参数: count, 值不能超过 source 的长度 - %d", count));
        }

        if (count == 0) {
            return new byte[0];
        }
        byte[] newArray = new byte[count];
        System.arraycopy(source, source.length - count, newArray, 0, newArray.length);
        return newArray;
    }

    /**
     * 从数组的结尾返回指定数量的连续元素
     *
     * @param source 返回元素的字节序列
     * @param count  要返回的元素数量
     * @return 一个字节数组, 包含从输入数组结尾的指定数量的元素
     * @throws NullPointerException     source 是 null
     * @throws IllegalArgumentException count 不能是负数且不能大于 source.length
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] right(Class<T> tClass, T[] source, int count) {
        if (source == null) {
            throw new NullPointerException("参数: source, 值不能为空");
        }

        if (count < 0) {
            throw new IllegalArgumentException("参数: count, 值不能为负数");
        }

        if (count > source.length) {
            throw new IllegalArgumentException(String.format("参数: count, 值不能超过 source 的长度 - %d", count));
        }

        if (count == 0) {
            return (T[]) Array.newInstance(tClass, 0);
        }
        T[] newArray = (T[]) Array.newInstance(tClass, count);
        System.arraycopy(source, source.length - count, newArray, 0, newArray.length);
        return newArray;
    }

    /**
     * 将对象字节数组转换为原语
     *
     * @param array 一个 {@link Byte} 数组
     * @return 一个 {@code byte} 数组, 如果 array 是 null, 则返回 null
     * @throws NullPointerException if array content is {@code null}
     */
    public static byte[] toPrimitive(Byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new NullPointerException("无法将 null 转换为 byte, index: " + i);
            }
            result[i] = array[i];
        }
        return result;
    }

    /**
     * 将原始字节数组转换为对象.
     *
     * @param array 一个 {@code byte} 数组
     * @return 一个 {@link Byte} 数组, 如果 array 是 null, 则返回 null
     */
    public static Byte[] toObject(byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        final Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}
