package jie.runtime.rpc.tcp;

import jie.runtime.io.BufferReader;
import jie.runtime.io.BufferWriter;
import jie.runtime.utils.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 表示消息分片缓存的类
 *
 * @author jiegg
 */
class FragmentCache {

    //region --常量--
    /**
     * 获取分片大小
     */
    public static final int FRAGMENT_SIZE = 60000;
    //endregion

    //region --字段--
    private final ReentrantLock lock;
    private final Map<byte[], CacheAssembler> fragments;
    //endregion

    //region --构造函数--

    /**
     * 初始化 {@link FragmentCache} 类的新实例
     */
    public FragmentCache() {
        this.lock = new ReentrantLock();
        this.fragments = new HashMap<>();
    }
    //endregion

    //region --公开方法--

    /**
     * 将消息分片推入管理器中
     *
     * @param fragment 消息分片
     */
    public void push(Fragment fragment) {
        try {
            this.lock.lock();
            if (!this.fragments.containsKey(fragment.getTag())) {
                this.fragments.put(fragment.getTag(), new CacheAssembler(fragment.getTag(), fragment.getCount()));
            }
            this.fragments.get(fragment.getTag()).set(fragment);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 从消息分片管理器中拉取已形成的完整数据包的数据
     *
     * @return 如果成功拉取到了数据则为 {@link Packet}, 否则为 <code>null</code>
     */
    public Packet pull() {
        try {
            this.lock.lock();

            for (byte[] key : this.fragments.keySet()) {
                if (this.fragments.get(key).isCompleted()) {
                    return this.fragments.get(key).get();
                }
            }

            return null;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 将封包数据包装成消息分片
     *
     * @param type 消息类型
     * @param tag  消息标识
     * @param data 消息原始数据
     * @return 一个 {@link Iterable} 包含切分好的消息分片 {@link Fragment}
     * @throws IOException I/O错误
     */
    public static Iterable<Fragment> createFragments(PacketType type, long tag, byte[] data) throws IOException {

        // 计算分片数量
        int count = data.length / FRAGMENT_SIZE;
        if ((count * FRAGMENT_SIZE) < data.length) {
            count += 1;
        }

        // 计算Tag
        BufferWriter tagBuf = new BufferWriter();
        tagBuf.write(type.getValue());
        tagBuf.write(tag);
        tagBuf.write(data);
        byte[] fragmentTag = tagBuf.toByteArray();

        // 对数据进行分片
        Fragment[] fragments = new Fragment[count];
        for (int i = 0; i < fragments.length; i++) {
            int len = FRAGMENT_SIZE;
            int offset = i * len;
            if (data.length - offset < len) {
                len = data.length - offset;
            }
            byte[] fragmentData = ArrayUtils.left(ArrayUtils.skip(data, offset), len);

            // 生成分片信息
            fragments[i] = new Fragment(fragmentTag, i, count, fragmentData);
        }
        return Arrays.asList(fragments);
    }
    //endregion

    //region --内部类--
    static class CacheAssembler {

        //region --字段--
        private PacketType type;
        private final long tag;
        private final Fragment[] fragments;
        private int count;
        //endregion

        //region --构造函数--
        public CacheAssembler(byte[] tag, int count) {

            BufferReader reader = new BufferReader(tag);

            // 读取类型
            byte typeByte = reader.readByte();
            Arrays.stream(PacketType.values())
                    .filter(p -> p.getValue() == typeByte)
                    .findFirst()
                    .ifPresent(p -> this.type = p);
            // 读取tag
            this.tag = reader.readInt64();

            this.count = 0;
            this.fragments = new Fragment[count];
        }
        //endregion

        //region --公开方法--
        public void set(Fragment fragment) {
            this.fragments[fragment.getIndex()] = fragment;
            this.count += 1;
        }

        public Packet get() {
            byte[][] temp = new byte[this.fragments.length][];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = this.fragments[i].getData();
            }
            return new Packet(this.type, this.tag, ArrayUtils.concat(temp));
        }

        public boolean isCompleted() {
            return this.count == this.fragments.length;
        }
        //endregion
    }
    //endregion
}
