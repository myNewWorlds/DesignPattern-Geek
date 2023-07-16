package com.example;

import com.example.util.UrlUtil;

public class ApiRequest {
    public static final String TOKEN_PARAM = "token";
    public static final String APP_ID_PARAM = "appId";
    public static final String TIMESTAMP_PARAM = "timestamp";
    private final String baseUrl;
    private final String token;
    private final String appId;
    private final long timestamp;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }

    public String getAppId() {
        return appId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ApiRequest(String baseUrl, String token, String appId, long timestamp) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.appId = appId;
        this.timestamp = timestamp;
    }

    //解析url
    public static ApiRequest createFromFullUrl(String url) {
        UrlUtil.UrlEntity urlEntity = UrlUtil.parse(url);
        return new ApiRequest(
                urlEntity.baseUrl,
                urlEntity.getParams(TOKEN_PARAM),
                urlEntity.getParams(APP_ID_PARAM),
                Long.parseLong(urlEntity.getParams(TIMESTAMP_PARAM))
        );
    }
}
