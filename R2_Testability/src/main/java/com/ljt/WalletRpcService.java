package com.ljt;

public class WalletRpcService {
    public String moveMoney(String id, Long buyerId, Long sellerId) {
        //模拟转移钱服务不可用
        throw new RuntimeException("模拟转移钱服务不可用");
    }
}
