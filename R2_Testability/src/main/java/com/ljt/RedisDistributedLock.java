package com.ljt;

public class RedisDistributedLock {
    private static final Redis redis;

    static {
        redis = new Redis();
    }

    //单例模式
    public static Redis getSingletonIntance() {
        return redis;
    }
}
