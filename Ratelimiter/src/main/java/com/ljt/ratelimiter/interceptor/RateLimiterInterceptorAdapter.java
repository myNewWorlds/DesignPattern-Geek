package com.ljt.ratelimiter.interceptor;

import com.ljt.ratelimiter.rule.ApiLimit;

/**
 * 抽象类，简化接口实现
 * 方便有些方法设置前拦截器或后拦截器
 */
public abstract class RateLimiterInterceptorAdapter implements RateLimiterInterceptor {
    @Override
    public void beforeLimit(String appId, String api) {

    }

    @Override
    public void afterLimit(String appId, String api, ApiLimit apiLimit, boolean result, Exception e) {

    }
}
