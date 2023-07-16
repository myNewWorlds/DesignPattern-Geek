package com.example;

import com.example.inter.CredentialStorage;

public class MysqlCredentialStorage implements CredentialStorage {

    @Override
    public String getPasswordByAppId(String appId) {
        return "123456";
    }
}
