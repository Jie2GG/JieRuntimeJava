package jie.runtime.rpc;

/**
 * 表示远程调用服务提供方法转换服务的类, 该类是抽象的
 *
 * @author jiegg
 */
public abstract class RpcMethodConverter {

    /**
     * 将远程调用指定的方法根据规则转换为本地可识别的方法
     *
     * @param methodName 方法名称
     * @return 被转换的方法名称
     */
    public abstract String convert(String methodName);


}
