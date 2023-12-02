package com.ljt.ratelimiter.monitor;

import java.util.concurrent.atomic.LongAdder;

public class MetricsCounter {
    //类似AtomicLong，线程安全的计数器
    private final LongAdder[] counters;

    public MetricsCounter() {
        int length = MetricType.values().length;
        counters = new LongAdder[length];
        //对象数组的每个对象实例化
        //必须用fori进行初始化，foreach不能修改元素的值
        for (int i = 0; i < length; i++) {
            counters[i] = new LongAdder();
        }
    }

    /**
     * 为一组性能指标计数加一
     */
    public void increment(MetricType... types) {
        for (MetricType type : types) {
            getCounter(type).increment();
        }
    }

    public void add(MetricType type, long value) {
        getCounter(type).add(value);
    }

    /**
     * 得到当前计数并重置
     */
    public long sumAndReset(MetricType type) {
        return getCounter(type).sumThenReset();
    }

    /**
     * 根据{@link MetricType}类型，得到对应的计数器
     */
    private LongAdder getCounter(MetricType type) {
        return counters[type.ordinal()];
    }
}
