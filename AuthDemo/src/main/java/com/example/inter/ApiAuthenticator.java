package com.example.inter;

import com.example.ApiRequest;

public interface ApiAuthenticator {
    void auth(String url);

    void auth(ApiRequest apiRequest);
}
