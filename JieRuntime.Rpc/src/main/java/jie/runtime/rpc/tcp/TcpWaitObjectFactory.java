package jie.runtime.rpc.tcp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.locks.LockSupport;

/**
 * 提供 TCP 协议等待对象的工厂类
 */
class TcpWaitObjectFactory extends BasePooledObjectFactory<TcpWait> {

    //region --公开方法--

    /**
     * 创建 {@link TcpWait}
     *
     * @return 新创建的 {@link TcpWait}
     */
    @Override
    public TcpWait create() {
        return new TcpWait();
    }

    /**
     * 封装为池化对象
     *
     * @param obj 被池化的实例
     * @return 被池化后的对象
     */
    @Override
    public PooledObject<TcpWait> wrap(TcpWait obj) {
        return new DefaultPooledObject<>(obj);
    }

    /**
     * 在将对象返回到池时运行一些处理, 可用于重置对象的状态并指示是否应将对象返回到池中
     *
     * @param p 要返回到池的对象
     */
    @Override
    public void passivateObject(PooledObject<TcpWait> p) throws Exception {
        TcpWait wait = p.getObject();
        // 释放线程
        if (wait.getWaitThread() != null) {
            LockSupport.unpark(wait.getWaitThread());
        }

        // 初始化 TcpWait 对象
        wait.setResponse(false);
        wait.setResult(null);
        wait.setWaitThread(null);
    }
    //endregion
}
