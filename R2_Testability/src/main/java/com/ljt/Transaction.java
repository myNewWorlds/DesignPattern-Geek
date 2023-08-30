package com.ljt;

public class Transaction {
    private final long createTimestamp;
    private String id;
    private final Long buyerId;
    private final Long sellerId;
    private STATUS status;
    //钱包服务
    private WalletRpcService walletRpcService;
    //多线程锁服务
    private TransactionLock lock;

    public STATUS getStatus() {
        return status;
    }

    public void setWalletRpcService(WalletRpcService walletRpcService) {
        this.walletRpcService = walletRpcService;
    }

    public void setTransactionLock(TransactionLock lock) {
        this.lock = lock;
    }

    public Transaction(String preAssignedId, Long buyerId, Long sellerId, Long productId, Long orderId) {
        if (preAssignedId != null && !preAssignedId.isEmpty()) {
            this.id = preAssignedId;
        } else {
            this.id = IdGenerator.generateTransactionId();
        }
        if (!this.id.startsWith("t_")) {
            this.id = "t_" + preAssignedId;
        }
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.status = STATUS.TO_BE_EXECUTD;
        this.createTimestamp = System.currentTimeMillis();
    }

    //将包含时间的未决行为逻辑，重新封装
    //（TransactionLock是不是也可以使用这种方式，而不是注入一个类）
    protected boolean isExpired() {
        long executionInvokedTimestamp = System.currentTimeMillis();
        return executionInvokedTimestamp - createTimestamp > 10000;
    }

    public boolean execute() throws InvalidTransactionException {
        if (buyerId == null || sellerId == null) {
            throw new InvalidTransactionException();
        }
        if (status == STATUS.EXECUTED) {
            return true;
        }
        boolean isLocked = false;
        try {
            isLocked = lock.lock(id);
            if (!isLocked) {
                return false; //锁定未成功，返回false
            }
            //多线程情况下，对状态进行double check
            if (status == STATUS.EXECUTED) {
                return true;
            }
            if (isExpired()) {
                this.status = STATUS.EXPIRED;
                return false;
            }
            String walletTransactionId = walletRpcService.moveMoney(id, buyerId, sellerId);
            if (walletTransactionId != null) {
                this.status = STATUS.EXECUTED;
                return true;
            } else {
                this.status = STATUS.FAILED;
                return false;
            }
        } finally {
            if (isLocked) {
                lock.unlock(id);
            }
        }
    }
}
