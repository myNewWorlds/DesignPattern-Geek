import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 实现线程唯一的单例对象
 * 通过HashMap判断线程id，从而保证单例对象的线程唯一性
 */
public class IdGenerator {
    private AtomicLong id = new AtomicLong(0);
    private static final ConcurrentHashMap<Long, IdGenerator> instances = new ConcurrentHashMap<>();
    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        long currentThreadId = Thread.currentThread().getId();
        instances.putIfAbsent(currentThreadId, new IdGenerator());
        return instances.get(currentThreadId);
    }

    //ID生成器生成id
    public long getId() {
        return id.incrementAndGet();
    }
}
