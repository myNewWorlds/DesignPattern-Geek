package com.ljt.ratelimiter;

import com.ljt.ratelimiter.exception.InternalErrorException;
import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.exception.OverloadException;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptor;

import java.util.List;

/**
 * 串联整个限流流程（接口）
 */
public interface UrlRateLimiter {

    /**
     * 检查指定的url是否超过最大流量限制
     * （为什么不返回布尔值，而要抛出异常）
     * @param appId appID
     * @param url 请求url
     * @throws OverloadException url超过最大流量
     * @throws InvalidUrlException url无效
     * @throws InternalErrorException 各种内部错误
     */
    void limit(String appId, String url) throws OverloadException, InvalidUrlException, InternalErrorException;

    /**
     * 增加拦截器到默认的拦截器链，这个拦截器将在{@link UrlRateLimiter#limit(String, String)}方法的前后do some work
     */
    void addInterceptor(RateLimiterInterceptor interceptor);

    /**
     * 增加一组拦截器到默认的拦截器链，
     */
    void addInterceptors(List<RateLimiterInterceptor> interceptors);
}
