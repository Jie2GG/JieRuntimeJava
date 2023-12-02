package jie.runtime.rpc.event;

/**
 * 提供远程调用服务客户端事件监听服务的接口
 *
 * @author jiegg
 */
public interface IRpcClientEvent {

    /**
     * 表示远程调用客户端成功连接到服务端的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onConnected(Object sender, RpcEventArgs args);

    /**
     * 表示远程调用客户端断开与服务端断开连接的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onDisconnected(Object sender, RpcEventArgs args);

    /**
     * 表示远程调用客户端出现异常的事件
     *
     * @param sender 引发此事件的事件源
     * @param args   事件参数
     */
    void onException(Object sender, RpcExceptionEventArgs args);
}
