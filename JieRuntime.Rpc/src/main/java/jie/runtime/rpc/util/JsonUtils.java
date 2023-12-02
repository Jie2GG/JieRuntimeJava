package jie.runtime.rpc.util;

import com.alibaba.fastjson2.JSON;

import java.nio.charset.StandardCharsets;

/**
 * Json 工具
 *
 * @author jiegg
 */
public class JsonUtils {

    public static <T> T deserialize(byte[] utf8Json, Class<T> tClass) {
        return deserialize(new String(utf8Json, StandardCharsets.UTF_8), tClass);
    }

    public static <T> T deserialize(String json, Class<T> tClass) {
        return JSON.parseObject(json, tClass);
    }

    public static <T> byte[] serializeToUtf8Bytes(T value) {
        return serialize(value).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> String serialize(T value) {
        return JSON.toJSONString(value);
    }
}
