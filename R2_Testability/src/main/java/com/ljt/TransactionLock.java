package com.ljt;

/**
 * 重新封装RedisDistributedLock，方便进行测试
 */
public class TransactionLock {
    public boolean lock(String id) {
        return RedisDistributedLock.getSingletonIntance().lockTransction(id);
    }
    public void unlock(String id) {
        RedisDistributedLock.getSingletonIntance().unlockTransction(id);
    }
}
