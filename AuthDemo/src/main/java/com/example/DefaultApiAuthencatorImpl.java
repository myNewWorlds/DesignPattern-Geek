package com.example;

import com.example.inter.ApiAuthenticator;
import com.example.inter.CredentialStorage;

public class DefaultApiAuthencatorImpl implements ApiAuthenticator {
    private final CredentialStorage credentialStorage;

    public DefaultApiAuthencatorImpl() {
        credentialStorage = new MysqlCredentialStorage();
    }

    @Override
    public void auth(String url) {
        ApiRequest apiRequest = ApiRequest.createFromFullUrl(url);
        auth(apiRequest);
    }

    @Override
    public void auth(ApiRequest apiRequest) {
        String token = apiRequest.getToken();
        String baseUrl = apiRequest.getBaseUrl();
        String appId = apiRequest.getAppId();
        long timestamp = apiRequest.getTimestamp();
        AuthToken clientAuthToken = new AuthToken(token, timestamp);
        if (clientAuthToken.isExpired()) {
            throw new RuntimeException("Token is expired.");
        }
        String password = credentialStorage.getPasswordByAppId(appId);
        AuthToken authToken = AuthToken.create(baseUrl, appId, password, timestamp);
        if (!authToken.match(clientAuthToken)) {
            throw new RuntimeException("Token verification fail");
        }
    }
}
