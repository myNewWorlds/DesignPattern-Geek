import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通过 LinkedHashMap 实现 LRU算法
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private static final long serialVersionUID = 1L;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当HashMap的size大于预设的最大容纳元素数量maxElements时移除旧的元素
        return this.size() > maxElements;
    }

    public static void print(LRUCache<String, Integer> cache) {
        cache.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    public static void main(String[] args) {
        //设置HashMap的容量为3
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("k1", 1);
        cache.put("k2", 2);
        cache.put("k3", 3);
        print(cache);

        cache.get("k3");
        cache.get("k2");
        cache.get("k1");  //k1 是最新的调用， k3 是最旧的调用
        print(cache);

        cache.put("k4", 4); //k3 被覆盖移除
        print(cache);
    }
}
