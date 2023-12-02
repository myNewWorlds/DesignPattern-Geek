package com.ljt.ratelimiter.interceptor;

import com.ljt.ratelimiter.extension.OrderComparator;
import com.ljt.ratelimiter.rule.ApiLimit;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器链包含所有的拦截器
 */
public class RateLimiterInterceptorChain {
    private final List<RateLimiterInterceptor> interceptors;

    public RateLimiterInterceptorChain() {
        this.interceptors = new ArrayList<>();
    }

    public void doBeforeLimit(String appId, String url) {
        //按顺序正向执行
        for (RateLimiterInterceptor interceptor : interceptors) {
            interceptor.beforeLimit(appId, url);
        }
    }

    public void doAfterLimit(String appId, String url, ApiLimit apiLimit, boolean result, Exception e) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).afterLimit(appId, url, apiLimit, result, e);
        }
    }

    public List<RateLimiterInterceptor> getInterceptors() {
        return interceptors;
    }

    public void addInterceptor(RateLimiterInterceptor interceptor) {
        this.interceptors.add(interceptor);
        interceptors.sort(OrderComparator.INSTANCE);
    }

    public void addInterceptors(List<RateLimiterInterceptor> interceptors) {
        this.interceptors.addAll(interceptors);
        interceptors.sort(OrderComparator.INSTANCE);
    }

    public void clear() {
        this.interceptors.clear();
    }

    public boolean isEmpty() {
        return interceptors.isEmpty();
    }
}
