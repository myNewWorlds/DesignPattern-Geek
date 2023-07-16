package com.example;

import com.example.util.SHAUtil;

public class AuthToken {
    private static final long DEFAULT_EXPIRED_TIME_INTERVAL = 60 * 1000;
    private final String token;
    private final long createTime;
    private long expiredTimeInterval = DEFAULT_EXPIRED_TIME_INTERVAL;

    public String getToken() {
        return token;
    }

    public AuthToken(String token, long createTime) {
        this.token = token;
        this.createTime = createTime;
    }

    public static AuthToken create(String baseUrl, String appId, String password, long timestamp) {
        String token = SHAUtil.SHA(baseUrl + appId + password + timestamp);
        return new AuthToken(token, timestamp);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > createTime + expiredTimeInterval;
    }

    public boolean match(AuthToken clientAuthToken) {
        return token.equals(clientAuthToken.token);
    }
}
