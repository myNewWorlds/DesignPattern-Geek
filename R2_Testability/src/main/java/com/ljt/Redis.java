package com.ljt;

public class Redis {
    public boolean lockTransction(String id) {
        //模拟Redis服务不可用
        throw new RuntimeException("Redis服务不可用");
    }

    public void unlockTransction(String id) {
        throw new RuntimeException("Redis服务不可用");
    }
}
