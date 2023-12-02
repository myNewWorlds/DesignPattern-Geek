package com.ljt.ratelimiter.interceptor;

import com.ljt.ratelimiter.extension.Order;
import com.ljt.ratelimiter.monitor.MonitorManager;
import com.ljt.ratelimiter.rule.ApiLimit;

/**
 * 这个拦截器用来监视每个方法的调用时间
 */
@Order()
public class MonitorInterceptor extends RateLimiterInterceptorAdapter implements RateLimiterInterceptor{

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void beforeLimit(String appId, String api) {
        startTime.set(System.nanoTime());
    }

    @Override
    public void afterLimit(String appId, String api, ApiLimit apiLimit, boolean result, Exception e) {
        long startNano = startTime.get();
        startTime.remove();
        long duration = (System.nanoTime() - startNano) / 1000;
        MonitorManager.collect(appId, api, apiLimit, duration, result, e);
    }
}
