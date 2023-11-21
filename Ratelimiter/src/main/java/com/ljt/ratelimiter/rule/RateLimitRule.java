package com.ljt.ratelimiter.rule;

/**
 * 将解析yaml后的规则存储为 Trie树（提高URL的匹配速度）
 */
public class RateLimitRule {

    public RateLimitRule(RuleConfig ruleConfig) {

    }

    /**
     * 得到appId与api对应的限制规则
     * @param appId
     * @param api
     * @return
     */
    public ApiLimit getLimit(String appId, String api) {
        return null;
    }
}
