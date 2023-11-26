package com.ljt.ratelimiter.algorithm;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.ljt.ratelimiter.exception.InternalErrorException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 固定窗口限流算法
 * 每个接口都要在内存中对应一个 RateLimiter 对象，记录在当前时间窗口内已经被访问的次数。
 * 写一个类之前，不知道怎么写的时候，就想这个类你会怎么用，然后，就知道你需要添加哪些字段
 */
public class FixedTimeWindowRateLimiter implements RateLimiter{
    //访问次数计数器
    private final AtomicInteger currentCount = new AtomicInteger(0);
    //最大限制次数
    private final int limit;
    private final Stopwatch stopwatch;

    /**
     * timeout for {@code Lock.tryLock()}
     */
    private static final long TRY_LOCK_TIMEOUT = 200L;
    private final Lock lock = new ReentrantLock();

    public FixedTimeWindowRateLimiter(int limit) {
        this(limit, Stopwatch.createStarted());
    }

    @VisibleForTesting
    public FixedTimeWindowRateLimiter(int limit, Stopwatch stopwatch) {
        this.limit = limit;
        this.stopwatch = stopwatch;
    }

    /**
     * 如果计数次数没超过limit，无论是否超出间隔时间，都允许访问
     * 如果计数次数超出limit且没超过一个段时间，则拒绝访问
     * 如果计数次数超出limit且超过一个段时间，则重置计数器，允许访问
     */
    @Override
    public boolean tryAcquire() throws InternalErrorException {
        if (currentCount.incrementAndGet() <= limit) {
            return true;
        }

        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(1)) {
                        currentCount.set(0);
                        stopwatch.reset();
                    }
                    //如果没超过时间间隔，再加一仍大于limit
                    //如果超过时间，重置为0后，再加一表示新间隔时间访问次数加一
                    return currentCount.incrementAndGet() <= limit;
                } finally {
                    //保证无论如何都能释放锁
                    lock.unlock();
                }
            } else {
                throw new InternalErrorException("tryAcquire() wait lock too long:" + TRY_LOCK_TIMEOUT + "ms");
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException("tryAcquire() is interrupted by lock-time-out." + e);
        }
    }
}
