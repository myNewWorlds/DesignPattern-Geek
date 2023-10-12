package com.ljt;

import java.util.concurrent.atomic.AtomicLong;

//基于枚举
public enum IdGenerator2 {
    INSTANCE;
    private AtomicLong id = new AtomicLong(0);

    public long getId() {
        return id.incrementAndGet();
    }
}
