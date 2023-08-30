package com.ljt.mock;

import com.ljt.WalletRpcService;

/**
 * 使转移钱返回正确的值
 */
public class MockWalletRpcServiceOne extends WalletRpcService {
    @Override
    public String moveMoney(String id, Long buyerId, Long sellerId) {
        return "12345";
    }
}
