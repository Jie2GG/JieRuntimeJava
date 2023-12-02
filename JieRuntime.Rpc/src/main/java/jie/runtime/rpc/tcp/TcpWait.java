package jie.runtime.rpc.tcp;

/**
 * 提供 TCP 等待服务的类
 */
class TcpWait {

    //region --字段--
    private boolean isResponse = false;
    private byte[] result = null;
    private Thread waitThread;
    //endregion

    //region --属性--

    /**
     * 获取一个 {@link Boolean} 值, 指示是否获得了响应
     */
    public boolean isResponse() {
        return isResponse;
    }

    /**
     * 设置一个 {@link Boolean} 值, 指示是否获得了响应
     *
     * @param value 一个 {@link Boolean} 值
     */
    public void setResponse(boolean value) {
        isResponse = value;
    }

    /**
     * 获取 TCP 等待的响应结果
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * 设置 TCP 等待的响应结果
     *
     * @param value 一个字节数组
     */
    public void setResult(byte[] value) {
        this.result = value;
    }

    /**
     * 获取 TCP 等待的线程
     */
    public Thread getWaitThread() {
        return waitThread;
    }

    /**
     * 设置 TCP 等待的线程
     *
     * @param value 一个 {@link Thread} 表示线程
     */
    public void setWaitThread(Thread value) {
        this.waitThread = value;
    }
    //endregion
}
