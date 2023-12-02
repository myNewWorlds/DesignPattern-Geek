package com.ljt.ratelimiter;

import com.ljt.ratelimiter.algorithm.RateLimiter;
import com.ljt.ratelimiter.exception.InternalErrorException;
import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.exception.OverloadException;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptor;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptorChain;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.rule.alg.RateLimitRule;
import com.ljt.ratelimiter.rule.source.RuleConfigSource;
import com.ljt.ratelimiter.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.ljt.ratelimiter.context.RateLimiterBeansFactory.BEANS_CONTEXT;

/**
 * 抽象类
 * 1. 简化接口的实现
 * 2. 为实现提供一些公用方法
 */
public abstract class AbstractUrlRateLimiter implements UrlRateLimiter {
    private static final Logger log = LoggerFactory.getLogger(AbstractUrlRateLimiter.class);
    //key为 appid:api
    //value为 限流计算的算法
    //以下为ConcurrentHashMap<String, AppUrlRateLimitRule>
    //key为 appid
    //value 为 限流规则树
    private final ConcurrentHashMap<String, RateLimiter> counters = new ConcurrentHashMap<>(256);
    //1. 直接传rule
    //2. 通过RuleConfigSource得到rule
    private final RateLimitRule rateLimitRule;

    private RateLimiterInterceptorChain interceptorChain;

    public AbstractUrlRateLimiter() {
        this((RateLimitRule) null);
    }

    public AbstractUrlRateLimiter(RuleConfigSource source) {
        this.rateLimitRule = BEANS_CONTEXT.obtainUrlRateLimitRule(null);
        source = BEANS_CONTEXT.obtainRuleConfigSource(source);
        this.rateLimitRule.addRule(source.load());
        this.interceptorChain = BEANS_CONTEXT.obtainInterceptorChain(null);
    }

    public AbstractUrlRateLimiter(RateLimitRule rule) {
        this(rule, null);
    }

    public AbstractUrlRateLimiter(RateLimitRule rule, RateLimiterInterceptorChain chain) {
        this.rateLimitRule = BEANS_CONTEXT.obtainUrlRateLimitRule(rule);
        if (rule == null) {
            /* load config from source and build rule. */
            RuleConfigSource source = BEANS_CONTEXT.obtainRuleConfigSource(null);
            this.rateLimitRule.addRule(source.load());
        }

        this.interceptorChain = BEANS_CONTEXT.obtainInterceptorChain(chain);
    }

    @Override
    public void addInterceptors(List<RateLimiterInterceptor> interceptors) {
        if (interceptors != null && !interceptors.isEmpty()) {
            this.interceptorChain.addInterceptors(interceptors);
        }
    }

    @Override
    public void addInterceptor(RateLimiterInterceptor interceptor) {
        if (interceptor != null) {
            this.interceptorChain.addInterceptor(interceptor);
        }
    }

    @Override
    public void limit(String appId, String url) throws OverloadException, InvalidUrlException, InternalErrorException {
        //先执行拦截链前置操作
        interceptorChain.doBeforeLimit(appId, url);

        ApiLimit apiLimit = null;
        boolean passed = false;
        Exception exception = null;
        try {
            String urlPath = UrlUtils.getUrlPath(url);
            apiLimit = rateLimitRule.getLimit(appId, urlPath);
            if (apiLimit == null) {
                log.warn("no rate limit rule for api: {}", urlPath);
                return; // passed
            }
            RateLimiter rateLimiter = getRateLimiterAlgorithm(appId, apiLimit.getApi(), apiLimit.getLimit());
            passed = rateLimiter.tryAcquire();
            if (!passed) {
                String builder = appId + ":" + url +
                        " has exceeded max tps limit:" +
                        apiLimit;
                throw new OverloadException(builder);
            }
        } catch (OverloadException e) {
            throw e;
        } catch ( InvalidUrlException | InternalErrorException e) {
            exception = e;
            throw e;
        } catch (Exception e) {
            InternalErrorException re = new InternalErrorException("Rate limiter internal error.", e);
            exception = re;
            throw re;
        } finally {
            interceptorChain.doAfterLimit(appId, url, apiLimit, passed, exception);
        }

    }

    /**
     * 得到限制规则算法
     */
    private RateLimiter getRateLimiterAlgorithm(String appId, String api, int limit) {
        String limitKey = generateUrlKey(appId, api);
        RateLimiter rateLimiter = counters.get(limitKey);
        if (rateLimiter == null) {
            RateLimiter newRateLimiter = createRateLimitAlgorithm(limitKey, limit);
            rateLimiter = counters.putIfAbsent(limitKey, newRateLimiter);
            if (rateLimiter == null) {
                rateLimiter = newRateLimiter;
            }
        }
        return rateLimiter;
    }

    /**
     * 创建限流规则算法
     * @param limitKey the API key, such as "appid:api"
     * @param limit the max hit count limit per second.
     * @return the rate limit algorithm.
     */
    protected abstract RateLimiter createRateLimitAlgorithm(String limitKey, int limit);

    /**
     * 为每个appID和api规则生成唯一的key
     */
    private String generateUrlKey(String appId, String api) {
        return appId + ":" + api;
    }

}
