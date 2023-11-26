package com.ljt.ratelimiter.rule.alg;

import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;

import java.util.List;

/**
 * 实现将ApiLimit存储的数据结构，并支持CRUD操作
 * This interface stores rate limit rules and supply CRUD operation methods.
 */
public interface RateLimitRule {

    /**
     * get limit info of one url
     */
    ApiLimit getLimit(String appId, String api) throws InvalidUrlException;

    /**
     * add one limit for a app
     */
    void addLimit(String appId, ApiLimit apiLimit) throws InvalidUrlException;

    /**
     * add limits for a app
     */
    void addLimits(String appId, List<ApiLimit> limits) throws InvalidUrlException;

    /**
     * override old rule
     */
    void rebuildRule(UniformRuleConfigMapping uniformRuleConfigMapping);

    /**
     * add rule into the existing rule
     */
    void addRule(UniformRuleConfigMapping uniformRuleConfigMapping);
}
