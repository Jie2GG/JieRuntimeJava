package jie.runtime.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import jie.runtime.BinaryConvert;


/**
 * 提供一组快速处理 UUID 的方法
 *
 * @author jiegg
 */
public class GuidUtils {

    /**
     * 创建一个新的 GUID 并转换为 64 位有符号整数
     *
     * @return 一个 64 位有符号证书, 来源于新 GUID 的一部分
     */
    public static long newGuidInt64() {
        return BinaryConvert.toInt64(UUIDUtil.asByteArray(Generators.randomBasedGenerator().generate()));
    }

}
