package jie.runtime.net.sockets.tcp;

import java.util.ArrayList;

/**
 * 作用于缓存的集合
 *
 * @author jiegg
 */
class CacheList extends ArrayList<Byte> {

    /**
     * 移除一定范围内的数据
     *
     * @param index 从指定的位置开始
     * @param count 要移除的元素数量
     */
    @Override
    public void removeRange(int index, int count) {
        if (index < 0) {
            throw new IllegalArgumentException("参数: index, 要求非负数");
        }

        if (count < 0) {
            throw new IllegalArgumentException("参数: count, 要求非负数");
        }

        if (this.size() - index < count) {
            throw new IndexOutOfBoundsException("偏移量和长度超出数组的范围, 或者计数大于从索引到源集合末尾的元素数量");
        }

        if (count > 0) {
            int end = index + count;
            super.removeRange(index, end);
        }
    }
}
