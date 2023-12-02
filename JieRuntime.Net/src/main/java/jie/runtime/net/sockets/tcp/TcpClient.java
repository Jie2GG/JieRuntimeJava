package jie.runtime.net.sockets.tcp;


import jie.runtime.BinaryConvert;
import jie.runtime.net.sockets.SocketClient;
import jie.runtime.utils.ArrayUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供基于 TCP 协议的网络客户端
 *
 * @author jiegg
 */
public class TcpClient extends SocketClient<AsynchronousSocketChannel> {

    //region --常量--
    /**
     * 获取 TCP 协议网络客户端默认封包大小
     */
    public static final int DEFAULT_PACKET_SIZE = 65535;
    //endregion

    //region --字段--
    private AsynchronousSocketChannel client;
    private final ConnectedHandler clientConnectedHandler;
    private final ReceiveDataHandler clientReceiveHandler;
    private final SendDataHandler clientSendHandler;
    private final TcpCache cache;
    private final ExecutorService threadPool;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private boolean isRunning;
    private boolean isConnected;
    private int packetSize;
    private byte packetHeaderLength;
    //endregion

    //region --属性--

    /**
     * 获取基础网络套接字
     */
    @Override
    public AsynchronousSocketChannel getClient() {
        return this.client;
    }

    /**
     * 获取当前网络客户端的本地 IP 地址
     */
    @Override
    public InetSocketAddress getLocalAddress() {
        return this.localAddress;
    }

    /**
     * 获取当前网络客户端的远程 IP 地址
     */
    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    /**
     * 获取当前客户端是否正在运行
     */
    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * 获取当前客户端是否已连接到远程服务器
     */
    @Override
    public boolean isConnected() {
        return this.isConnected;
    }

    /**
     * 获取当前客户端接收或发送数据时的封包大小
     */
    @Override
    public int getPacketSize() {
        return this.packetSize;
    }

    /**
     * 设置当前客户端接收或发送数据时的封包大小
     *
     * @param value 一个 32 位整数, 指定封包大小
     */
    @Override
    public void setPacketSize(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("参数: value, 封包大小不能小于 1");
        }
        this.packetSize = value;
        long len = this.packetSize;
        byte size = 0;
        do {
            len >>>= 8;
            size++;
        } while (len > 0);
        this.packetHeaderLength = size;
        if (this.cache != null) {
            this.cache.setPacketHeaderLength(this.packetHeaderLength);
        }
    }

    /**
     * 获取当前客户端接收或发送数据时的封包头占用字节数
     */
    @Override
    public byte getPacketHeaderLength() {
        return this.packetHeaderLength;
    }
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link TcpClient} 类的新实例
     */
    public TcpClient() {
        this.clientConnectedHandler = new ConnectedHandler();
        this.clientReceiveHandler = new ReceiveDataHandler();
        this.clientSendHandler = new SendDataHandler();

        this.setPacketSize(DEFAULT_PACKET_SIZE);
        this.cache = new TcpCache(this.getPacketHeaderLength());

        // 初始化线程池
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * 使用已初始化的 {@link  AsynchronousSocketChannel} 来初始化 {@link TcpClient} 类的新实例
     *
     * @param socket 已初始化的 {@link AsynchronousSocketChannel}
     */
    TcpClient(AsynchronousSocketChannel socket) {
        this();

        if (socket == null) {
            throw new NullPointerException("参数: socket 是 null");
        }

        this.client = socket;
        if (this.client.isOpen()) {
            // 设置客户端状态
            this.isRunning = true;
            this.isConnected = true;

            try {
                SocketAddress localAddr = this.client.getLocalAddress();
                if (localAddr instanceof InetSocketAddress) {
                    TcpClient.this.localAddress = (InetSocketAddress) localAddr;
                }
                SocketAddress remoteAddr = this.client.getRemoteAddress();
                if (remoteAddr instanceof InetSocketAddress) {
                    TcpClient.this.remoteAddress = (InetSocketAddress) remoteAddr;
                }

                // 开始接收数据
                ByteBuffer buffer = ByteBuffer.allocate(this.getPacketSize());
                this.getClient().read(buffer, buffer, this.clientReceiveHandler);
            } catch (IOException e) {
                this.invokeExceptionEvent(e);
            }
        }
    }
    //endregion

    //region --公开方法--

    /**
     * 连接远程客户端
     *
     * @param remoteAddr 远程服务端点
     */
    @Override
    public void connect(InetSocketAddress remoteAddr) {
        if (remoteAddr == null) {
            throw new NullPointerException("参数: remoteAddr 为 null");
        }

        if (!this.isRunning) {
            this.isRunning = true;
            try {
                // 如果有必要, 初始化客户端
                if (this.client == null) {
                    this.client = AsynchronousSocketChannel.open();
                }
                // 开始连接远程客户端
                this.client.connect(remoteAddr, null, this.clientConnectedHandler);
            } catch (IOException e) {
                super.invokeExceptionEvent(e);
            }
        }
    }

    /**
     * 断开连接远程客户端
     *
     * @param reuseClient 是否需要复用当前客户端
     */
    @Override
    public void disconnect(boolean reuseClient) {
        if (this.isRunning) {
            this.isRunning = false;
            try {
                // 关闭套接字
                if (this.client != null) {
                    this.client.close();
                }
                // 设置客户端状态
                this.isConnected = false;
                // 触发客户端断开事件
                this.invokeDisconnectedEvent();
            } catch (IOException e) {
                this.invokeExceptionEvent(e);
            } finally {
                if (reuseClient) {
                    this.client = null;
                }
            }
        }
    }

    /**
     * 发送数据到远程客户端
     *
     * @param data 要发送的数据
     */
    @Override
    public void send(byte[] data) {
        if (this.isRunning() && this.isConnected()) {
            if (data == null) {
                throw new NullPointerException("参数: data 为 null");
            }

            if (data.length > (this.getPacketSize() - this.getPacketHeaderLength())) {
                throw new IllegalArgumentException("参数: data, 要发送的数据包大小超过了上限: " + (this.getPacketSize() - this.getPacketHeaderLength()));
            }

            try {
                // 计算封包包头
                int len = data.length + this.getPacketHeaderLength();
                byte[] lenBytes = BinaryConvert.getBytes(len, true);

                // 填充包头
                ByteBuffer buffer = ByteBuffer.allocate(len);
                buffer.put(ArrayUtils.right(lenBytes, this.getPacketHeaderLength()));
                buffer.put(data);
                buffer.flip();

                // 发送数据
                this.client.write(buffer, buffer, this.clientSendHandler);
            } catch (Exception e) {
                this.invokeExceptionEvent(e);
            }
        }
    }

    /**
     * 释放当前实例所占用的资源
     */
    @Override
    public void close() {
        super.close();
        if (this.isRunning) {
            this.disconnect(false);
        }
    }
    //endregion

    //region --内部类--
    private class ConnectedHandler implements CompletionHandler<Void, Void> {

        @Override
        public void completed(Void result, Void attachment) {

            // 修改客户端信息
            TcpClient.this.isConnected = true;
            try {
                SocketAddress localAddr = TcpClient.this.client.getLocalAddress();
                if (localAddr instanceof InetSocketAddress) {
                    TcpClient.this.localAddress = (InetSocketAddress) localAddr;
                }
                SocketAddress remoteAddr = TcpClient.this.client.getRemoteAddress();
                if (remoteAddr instanceof InetSocketAddress) {
                    TcpClient.this.remoteAddress = (InetSocketAddress) remoteAddr;
                }

                // 触发客户端连接事件
                TcpClient.this.invokeConnectedEvent();

                // 开始接收数据
                if (TcpClient.this.isRunning() && TcpClient.this.isConnected()) {
                    ByteBuffer buffer = ByteBuffer.allocate(TcpClient.this.getPacketSize());
                    TcpClient.this.getClient().read(buffer, buffer, TcpClient.this.clientReceiveHandler);
                }

            } catch (IOException e) {
                TcpClient.this.invokeExceptionEvent(e);
            }
        }


        @Override
        public void failed(Throwable exc, Void attachment) {
            TcpClient.this.invokeExceptionEvent(exc);
        }
    }

    private class ReceiveDataHandler implements CompletionHandler<Integer, ByteBuffer> {

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (TcpClient.this.isRunning() && TcpClient.this.isConnected() && result != null && result > 0) {

                try {
                    // 获取远端数据
                    byte[] data = new byte[result];
                    attachment.flip();
                    attachment.get(data, 0, data.length);

                    // 封包处理
                    TcpClient.this.cache.push(data);
                    if (TcpClient.this.cache.isPull()) {

                        // 异步启动调用数据接收事件
                        byte[] finalData = TcpClient.this.cache.pull();
                        TcpClient.this.threadPool.submit(() -> TcpClient.this.invokeReceiveDataEvent(finalData));
                    }

                } catch (Exception e) {
                    TcpClient.this.invokeExceptionEvent(e);
                } finally {
                    // 继续接收
                    if (TcpClient.this.isRunning() && TcpClient.this.isConnected()) {

                        attachment.clear();
                        TcpClient.this.getClient().read(attachment, attachment, TcpClient.this.clientReceiveHandler);
                    } else {
                        TcpClient.this.disconnect(true);
                    }
                }
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            TcpClient.this.invokeExceptionEvent(exc);
        }
    }

    private class SendDataHandler implements CompletionHandler<Integer, ByteBuffer> {

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (TcpClient.this.isRunning() && TcpClient.this.isConnected()) {
                try {
                    // 获取发送的数据
                    byte[] data = attachment.array();

                    // 处理发送结果
                    if (result != null && result > 0) {
                        // 触发数据送达事件
                        TcpClient.this.invokeSendDataEvent(data);
                    }

                } catch (Exception e) {
                    TcpClient.this.invokeExceptionEvent(e);
                }
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            TcpClient.this.invokeExceptionEvent(exc);
        }
    }
    //endregion
}
