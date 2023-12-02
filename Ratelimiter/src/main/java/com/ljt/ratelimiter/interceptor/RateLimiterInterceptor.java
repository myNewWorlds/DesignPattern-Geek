package com.ljt.ratelimiter.interceptor;

import com.ljt.ratelimiter.rule.ApiLimit;

/**
 * 拦截器接口
 */
public interface RateLimiterInterceptor {

    /**
     * 在{@link com.ljt.ratelimiter.UrlRateLimiter#limit(String, String)}前执行
     * @param appId the app ID
     * @param api the API
     */
    void beforeLimit(String appId, String api);

    /**
     * 在{@link com.ljt.ratelimiter.UrlRateLimiter#limit(String, String)}后执行
     * @param appId the app ID
     * @param api the API
     * @param apiLimit apiLimit contains all limit info, refer to {@link ApiLimit}
     * @param result result true if {@link com.ljt.ratelimiter.UrlRateLimiter#limit(String, String)} get an access token
     * @param e exception from {@link com.ljt.ratelimiter.UrlRateLimiter#limit(String, String)}
     */
    void afterLimit(String appId, String api, ApiLimit apiLimit, boolean result, Exception e);
}
