package jie.runtime;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 提供基本数据类型和二进制流的转换服务
 *
 * @author jiegg
 */
public class BinaryConvert {

    //region --公开方法--

    /**
     * 返回从字节数组中指定位置的一个字节转换而来的 {@link Boolean} 值
     *
     * @param bytes 一个字节数组
     * @return 由一个字节构成的布尔值
     */
    public static boolean toBoolean(byte[] bytes) {
        return toBoolean(bytes, 0);
    }

    /**
     * 返回从字节数组中指定位置的一个字节转换而来的 {@link Boolean} 值
     *
     * @param bytes      一个字节数组
     * @param startIndex bytes 中字节的索引
     * @return 由一个字节构成的布尔值
     */
    public static boolean toBoolean(byte[] bytes, int startIndex) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }

        bytes = convertFormat(bytes, startIndex, Byte.BYTES, false);
        if (bytes.length != 1) {
            throw new IllegalArgumentException("参数 bytes 无法转换为 boolean 值");
        }
        return bytes[0] != 0;
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 Unicode 字符
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由两个字节构成的 Unicode 字符
     * @throws NullPointerException bytes 是 null
     */
    public static char toChar(byte[] bytes) {
        return toChar(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 Unicode 字符
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由两个字节构成的 Unicode 字符
     * @throws NullPointerException bytes 是 null
     */
    public static char toChar(byte[] bytes, boolean reverse) {
        return toChar(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 Unicode 字符
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由两个字节构成的 Unicode 字符
     * @throws NullPointerException bytes 是 null
     */
    public static char toChar(byte[] bytes, int startIndex) {
        return toChar(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 Unicode 字符
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由两个字节构成的 Unicode 字符
     * @throws NullPointerException bytes 是 null
     */
    public static char toChar(byte[] bytes, int startIndex, boolean reverse) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }

        bytes = convertFormat(bytes, startIndex, Character.BYTES, reverse);
        convertReverse(bytes, reverse);

        // 获取数据类型一个 bit 的大小
        int bitSize = Character.SIZE / Character.BYTES;

        char result = 0;
        for (int i = bytes.length - 1; 0 <= i; i--) {
            result |= (char) ((bytes[i] & 0xff) << i * bitSize);
        }
        return result;
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 16 位有符号整数
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由两个字节构成的 16 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static short toInt16(byte[] bytes) {
        return toInt16(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 16 位有符号整数
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由两个字节构成的 16 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static short toInt16(byte[] bytes, boolean reverse) {
        return toInt16(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 16 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由两个字节构成的 16 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static short toInt16(byte[] bytes, int startIndex) {
        return toInt16(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的两个字节转换来的 16 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由两个字节构成的 16 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static short toInt16(byte[] bytes, int startIndex, boolean reverse) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }

        bytes = convertFormat(bytes, startIndex, Short.BYTES, reverse);
        convertReverse(bytes, reverse);

        // 获取数据类型一个 bit 的大小
        int bitSize = Short.SIZE / Short.BYTES;

        short result = 0;
        for (int i = bytes.length - 1; 0 <= i; i--) {
            result |= (short) ((bytes[i] & 0xff) << i * bitSize);
        }
        return result;
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的 32 位有符号整数
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由两个字节构成的 32 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static int toInt32(byte[] bytes) {
        return toInt32(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的 32 位有符号整数
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由两个字节构成的 32 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static int toInt32(byte[] bytes, boolean reverse) {
        return toInt32(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的 32 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由两个字节构成的 32 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static int toInt32(byte[] bytes, int startIndex) {
        return toInt32(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的 32 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由两个字节构成的 32 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static int toInt32(byte[] bytes, int startIndex, boolean reverse) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }

        bytes = convertFormat(bytes, startIndex, Integer.BYTES, reverse);
        convertReverse(bytes, reverse);

        // 获取数据类型一个 bit 的大小
        int bitSize = Integer.SIZE / Integer.BYTES;

        int result = 0;
        for (int i = bytes.length - 1; 0 <= i; i--) {
            result |= (bytes[i] & 0xff) << i * bitSize;
        }
        return result;
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的 64 位有符号整数
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由两个字节构成的 64 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static long toInt64(byte[] bytes) {
        return toInt64(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的 64 位有符号整数
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由两个字节构成的 64 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static long toInt64(byte[] bytes, boolean reverse) {
        return toInt64(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的 64 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由两个字节构成的 64 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static long toInt64(byte[] bytes, int startIndex) {
        return toInt64(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的 64 位有符号整数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由两个字节构成的 64 位有符号整数
     * @throws NullPointerException bytes 是 null
     */
    public static long toInt64(byte[] bytes, int startIndex, boolean reverse) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }

        bytes = convertFormat(bytes, startIndex, Long.BYTES, reverse);
        convertReverse(bytes, reverse);

        // 获取数据类型一个 bit 的大小
        int bitSize = Long.SIZE / Long.BYTES;

        long result = 0;
        for (int i = bytes.length - 1; 0 <= i; i--) {
            result |= ((long) (bytes[i] & 0xff) << i * bitSize);
        }
        return result;
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的单精度浮点数
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由四个字节构成的单精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static float toSingle(byte[] bytes) {
        return toSingle(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的单精度浮点数
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由四个字节构成的单精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static float toSingle(byte[] bytes, boolean reverse) {
        return toSingle(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的单精度浮点数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由四个字节构成的单精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static float toSingle(byte[] bytes, int startIndex) {
        return toSingle(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的四个字节转换来的单精度浮点数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由四个字节构成的单精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static float toSingle(byte[] bytes, int startIndex, boolean reverse) {
        int int32 = toInt32(bytes, startIndex, reverse);
        return Float.intBitsToFloat(int32);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的双精度浮点数
     *
     * @param bytes 指定数据存在的字节数组
     * @return 由八个字节构成的双精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static double toDouble(byte[] bytes) {
        return toDouble(bytes, 0, false);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的双精度浮点数
     *
     * @param bytes   指定数据存在的字节数组
     * @param reverse 是否反转数组进行数据读取
     * @return 由八个字节构成的双精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static double toDouble(byte[] bytes, boolean reverse) {
        return toDouble(bytes, 0, reverse);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的双精度浮点数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @return 由八个字节构成的双精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static double toDouble(byte[] bytes, int startIndex) {
        return toDouble(bytes, startIndex, false);
    }

    /**
     * 返回由字节数组中指定位置的八个字节转换来的双精度浮点数
     *
     * @param bytes      指定数据存在的字节数组
     * @param startIndex 从指定位置开始读取
     * @param reverse    是否反转数组进行数据读取
     * @return 由八个字节构成的双精度浮点数
     * @throws NullPointerException bytes 是 null
     */
    public static double toDouble(byte[] bytes, int startIndex, boolean reverse) {
        long int64 = toInt64(bytes, startIndex, reverse);
        return Double.longBitsToDouble(int64);
    }

    /**
     * 将指定字节数组中的所有字节解码为一个字符串
     *
     * @param bytes 包含要解码的字节序列的字节数组
     * @return 包含指定字节序列解码结果的字符串
     * @throws NullPointerException bytes 是 null
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 将指定字节数组中的所有字节解码为一个字符串
     *
     * @param bytes   包含要解码的字节序列的字节数组
     * @param charset 提供对字节序列的编码名称
     * @return 包含指定字节序列解码结果的字符串
     * @throws NullPointerException bytes 是 null
     */
    public static String toString(byte[] bytes, Charset charset) {
        if (bytes == null) {
            throw new NullPointerException("参数 bytes 为 null");
        }
        return new String(bytes, charset);
    }

    /**
     * 作为字节数组返回指定的 {@link Boolean} 值
     *
     * @param value 一个 {@link Boolean} 值
     * @return 长度为 1 的字节数组
     */
    public static byte[] getBytes(boolean value) {
        byte[] result = new byte[Byte.BYTES];
        result[0] = value ? (byte) 1 : (byte) 0;
        return result;
    }

    /**
     * 以字节数组的形式返回指定的 8 位无符号整数值
     *
     * @param value 要转换的数字
     * @return 长度为 1 的字节数组
     */
    public static byte[] getBytes(byte value) {
        return new byte[]{value};
    }

    /**
     * 以字节数组的形式返回指定的 Unicode 字符
     *
     * @param value 要转换的 Unicode 字符
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(char value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的 Unicode 字符
     *
     * @param value   要转换的 Unicode 字符
     * @param reverse 是否反序转换
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(char value, boolean reverse) {

        byte[] result = new byte[Character.BYTES];

        // 获取数据类型一个 bit 的大小
        int bitSize = Character.SIZE / Character.BYTES;

        for (int i = 0; i < result.length; i++) {
            if (reverse) {
                result[result.length - 1 - i] = (byte) (value >> (bitSize * i) & 0xff);
            } else {
                result[i] = (byte) (value >> (bitSize * i) & 0xff);
            }
        }

        return result;
    }

    /**
     * 以字节数组的形式返回指定的 16 位有符号整数值
     *
     * @param value 要转换的数字
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(short value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的 16 位有符号整数值
     *
     * @param value   要转换的数字
     * @param reverse 是否反序转换
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(short value, boolean reverse) {
        byte[] result = new byte[Short.BYTES];

        // 获取数据类型一个 bit 的大小
        int bitSize = Short.SIZE / Short.BYTES;

        for (int i = 0; i < result.length; i++) {
            if (reverse) {
                result[result.length - 1 - i] = (byte) (value >> (bitSize * i) & 0xff);
            } else {
                result[i] = (byte) (value >> (bitSize * i) & 0xff);
            }
        }

        return result;
    }

    /**
     * 以字节数组的形式返回指定的 32 位有符号整数值
     *
     * @param value 要转换的数字
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(int value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的 32 位有符号整数值
     *
     * @param value   要转换的数字
     * @param reverse 是否反序转换
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(int value, boolean reverse) {
        byte[] result = new byte[Integer.BYTES];

        // 获取数据类型一个 bit 的大小
        int bitSize = Integer.SIZE / Integer.BYTES;

        for (int i = 0; i < result.length; i++) {
            if (reverse) {
                result[result.length - 1 - i] = (byte) (value >> (bitSize * i) & 0xff);
            } else {
                result[i] = (byte) (value >> (bitSize * i) & 0xff);
            }
        }

        return result;
    }

    /**
     * 以字节数组的形式返回指定的 64 位有符号整数值
     *
     * @param value 要转换的数字
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(long value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的 64 位有符号整数值
     *
     * @param value   要转换的数字
     * @param reverse 是否反序转换
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(long value, boolean reverse) {
        byte[] result = new byte[Integer.BYTES];

        // 获取数据类型一个 bit 的大小
        int bitSize = Integer.SIZE / Integer.BYTES;

        for (int i = 0; i < result.length; i++) {
            if (reverse) {
                result[result.length - 1 - i] = (byte) (value >> (bitSize * i) & 0xff);
            } else {
                result[i] = (byte) (value >> (bitSize * i) & 0xff);
            }
        }

        return result;
    }

    /**
     * 以字节数组的形式返回指定的单精度浮点数
     *
     * @param value 要转换的数字
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(float value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的单精度浮点数
     *
     * @param value   要转换的数字
     * @param reverse 是否反序转换
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(float value, boolean reverse) {
        int intBits = Float.floatToRawIntBits(value);
        return getBytes(intBits, reverse);
    }

    /**
     * 以字节数组的形式返回指定的双精度浮点数
     *
     * @param value 要转换的数字
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(double value) {
        return getBytes(value, false);
    }

    /**
     * 以字节数组的形式返回指定的双精度浮点数
     *
     * @param value   要转换的数字
     * @param reverse 是否反序转换
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(double value, boolean reverse) {
        long longBits = Double.doubleToRawLongBits(value);
        return getBytes(longBits, reverse);
    }

    /**
     * 将指定字符串中的所有字符编码为一个字节序列
     *
     * @param value 包含要编码的字符的字符串
     * @return 一个字节数组，包含对指定的字符集进行编码的结果
     */
    public static byte[] getBytes(String value) {
        return getBytes(value, StandardCharsets.UTF_8);
    }

    /**
     * 将指定字符串中的所有字符编码为一个字节序列
     *
     * @param value   包含要编码的字符的字符串
     * @param charset 提供对字节序列的编码
     * @return 一个字节数组，包含对指定的字符集进行编码的结果
     */
    public static byte[] getBytes(String value, Charset charset) {
        return value.getBytes(charset);
    }

    /**
     * 将指定字节数组的每个元素的数值转换为它的等效十六进制字符串表示形式
     *
     * @param bytes 包含要编码为字符串的字节数组
     * @return 由以连字符分隔的十六进制对构成的字符串，其中每一对表示当前封包中对应的元素；例如“7F 2C 4A
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            int v = b & 0xFF;
            builder.append(String.format("%02X ", v));
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    /**
     * 将指定字符串中的所有十六进制值转换为它的等效字节数组表示形式
     *
     * @param hex 包含要转换为字节数组的十六进制字符串
     * @return 一个字节数组，包含对指定的十六进制字符串转换的结果
     */
    public static byte[] getHexBytes(String hex) {
        if ("".equals(hex) || hex == null) {
            throw new IllegalArgumentException("hex 不能为 null 或空白");
        }

        hex = hex.trim();
        boolean isMatch = Pattern.matches("^([A-Fa-f\\d]{2}\\s*)+$", hex);
        if (!isMatch) {
            throw new IllegalArgumentException("指定转换的字符串不是十六进制字符串");
        }

        String[] hexStr = hex.split(" ");
        byte[] result = new byte[hexStr.length];
        for (int i = 0; i < hexStr.length; i++) {
            result[i] = (byte) Integer.parseInt(hexStr[i], 16);
        }
        return result;
    }
    //endregion

    //region --私有方法--
    // 转换前反转
    private static void convertReverse(byte[] bytes, boolean reverse) {
        if (reverse) {

            // 双指针交换
            int start = 0;
            int end = bytes.length - 1;

            while (start < end) {
                byte temp = bytes[start];
                bytes[start] = bytes[end];
                bytes[end] = temp;
                start++;
                end--;
            }
        }
    }

    // 转换前格式化
    public static byte[] convertFormat(byte[] bytes, int startIndex, int len, boolean reverse) {

        byte[] temp = new byte[len];

        if (bytes.length - startIndex < len) {
            int copyLen = len - (len - (bytes.length - startIndex));
            if (reverse) {
                // 大端序, 缺字节, 左侧补0
                System.arraycopy(bytes, startIndex, temp, len - copyLen, copyLen);
            } else {
                // 小端序, 缺字节, 右侧补0
                System.arraycopy(bytes, startIndex, temp, 0, copyLen);
            }
        } else {
            System.arraycopy(bytes, startIndex, temp, 0, temp.length);
        }
        return temp;
    }
//endregion

}
