package com.ljt.ratelimiter.rule.alg;

import com.ljt.ratelimiter.exception.ConfigurationResolveException;
import com.ljt.ratelimiter.exception.InvalidUrlException;
import com.ljt.ratelimiter.extension.Order;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.rule.source.UniformRuleConfigMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用trie树存储 limit rule
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
public class UrlRateLimitRule implements RateLimitRule {

    //store <appId,limit rules>
    private ConcurrentHashMap<String, AppUrlRateLimitRule> limitRules =
            new ConcurrentHashMap<>();

    @Override
    public ApiLimit getLimit(String appId, String api) throws InvalidUrlException {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(api)) {
            return null;
        }
        AppUrlRateLimitRule appUrlRateLimitRule = limitRules.get(appId);
        if (appUrlRateLimitRule == null) {
            return null;
        }
        return appUrlRateLimitRule.getLimitInfo(api);
    }

    @Override
    public void addLimit(String appId, ApiLimit apiLimit) throws InvalidUrlException {
        if (StringUtils.isEmpty(appId) || apiLimit == null) {
            return;
        }
        AppUrlRateLimitRule trie = getAppUrlRateLimitRuleByAppId(appId);
        trie.addLimitInfo(apiLimit);
    }

    @Override
    public void addLimits(String appId, List<ApiLimit> limits) throws InvalidUrlException {
        if (StringUtils.isEmpty(appId)) {
            return;
        }
        AppUrlRateLimitRule trie = getAppUrlRateLimitRuleByAppId(appId);
        for (ApiLimit limit : limits) {
            trie.addLimitInfo(limit);
        }
    }

    /**
     * 整个HashMap重建
     */
    @Override
    public void rebuildRule(UniformRuleConfigMapping uniformRuleConfigMapping) {
        ConcurrentHashMap<String, AppUrlRateLimitRule> newLimitRules = new ConcurrentHashMap<>();
        List<UniformRuleConfigMapping.UniformRuleConfig> configs = uniformRuleConfigMapping.getConfigs();
        for (UniformRuleConfigMapping.UniformRuleConfig config : configs) {
            String appId = config.getAppId();
            AppUrlRateLimitRule trie = new AppUrlRateLimitRule();
            newLimitRules.put(appId, trie);
            try {
                for (ApiLimit limit : config.getLimits()) {
                    trie.addLimitInfo(limit);
                }
            } catch (InvalidUrlException e) {
                throw new ConfigurationResolveException("rule configuration is invalid: ", e);
            }
        }
        limitRules = newLimitRules;
    }

    @Override
    public void addRule(UniformRuleConfigMapping uniformRuleConfigMapping) {
        if (uniformRuleConfigMapping == null) {
            return;
        }
        List<UniformRuleConfigMapping.UniformRuleConfig> configs = uniformRuleConfigMapping.getConfigs();
        try {
            for (UniformRuleConfigMapping.UniformRuleConfig config : configs) {
                addLimits(config.getAppId(), config.getLimits());
            }
        } catch (InvalidUrlException e) {
            throw new ConfigurationResolveException("rule configuration is invalid: ", e);
        }
    }

    /**
     * 根据appId得到 AppUrlRateLimitRule（trie树）
     */
    private AppUrlRateLimitRule getAppUrlRateLimitRuleByAppId(String appId) {
        //为了线程安全不能使用contains判断
        //如果判断时不包含，但别的线程判断结束后添加，就会导致数据不一致
        AppUrlRateLimitRule newTrie = new AppUrlRateLimitRule();
        AppUrlRateLimitRule trie = limitRules.putIfAbsent(appId, newTrie);
        if (trie == null) {
            trie = newTrie;
        }
        return trie;
    }
}
