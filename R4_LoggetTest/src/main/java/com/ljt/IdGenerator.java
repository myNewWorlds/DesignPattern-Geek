package com.ljt;

import java.util.concurrent.atomic.AtomicLong;

//饿汉式加载
public class IdGenerator {
    //AtomicLong是线程安全的
    private AtomicLong id = new AtomicLong(0);
    private static final IdGenerator instance = new IdGenerator();
    private IdGenerator() {

    }

    public static IdGenerator getInstance() {
        return instance;
    }
    public long getId() {
        return id.incrementAndGet();
    }
}
