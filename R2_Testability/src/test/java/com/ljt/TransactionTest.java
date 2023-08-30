package com.ljt;

import com.ljt.mock.MockWalletRpcServiceOne;
import org.junit.Assert;
import org.junit.Test;

public class TransactionTest {
    //测试1：正常情况，交易完成
    @Test
    public void testExecute_1() throws InvalidTransactionException {
        Long buyerId = 123L;
        Long sellerId = 234L;
        Long productId = 345L;
        Long orderId = 456L;

        //创建TransactionLock的匿名子类
        TransactionLock lock = new TransactionLock() {
            @Override
            public boolean lock(String id) {
                return true;
            }
            @Override
            public void unlock(String id) {
            }
        };

        Transaction transaction = new Transaction(null, buyerId, sellerId, productId, orderId);
        transaction.setWalletRpcService(new MockWalletRpcServiceOne());
        transaction.setTransactionLock(lock);
        boolean executeResult = transaction.execute();
        Assert.assertTrue(executeResult);
        Assert.assertEquals(STATUS.EXECUTED, transaction.getStatus());
    }

    //测试2：buyerId、sellerId 为 null，返回 com.ljt.InvalidTransactionException
    @Test
    public void testExecute_2() {
        Long productId = 345L;
        Long orderId = 456L;
        //创建TransactionLock的匿名子类
        TransactionLock lock = new TransactionLock() {
            @Override
            public boolean lock(String id) {
                return true;
            }
            @Override
            public void unlock(String id) {
            }
        };

        Transaction transaction = new Transaction(null, null, null, productId, orderId);
        transaction.setWalletRpcService(new MockWalletRpcServiceOne());
        transaction.setTransactionLock(lock);
        Assert.assertThrows(InvalidTransactionException.class, transaction::execute);
    }

    //测试3：交易已过期（createTimestamp 超过 14 天），交易状态设置为 EXPIRED，返回 false
    @Test
    public void testExecute_3() throws InvalidTransactionException {
        Long buyerId = 123L;
        Long sellerId = 234L;
        Long productId = 345L;
        Long orderId = 456L;
        //创建TransactionLock的匿名子类
        TransactionLock lock = new TransactionLock() {
            @Override
            public boolean lock(String id) {
                return true;
            }

            @Override
            public void unlock(String id) {
            }
        };
        Transaction transaction = new Transaction(null, buyerId, sellerId, productId, orderId) {
            @Override
            protected boolean isExpired() {
                return true;
            }
        };
        transaction.setTransactionLock(lock);
        boolean actualResult = transaction.execute();
        Assert.assertFalse(actualResult);
        Assert.assertEquals(STATUS.EXPIRED, transaction.getStatus());
    }

}
