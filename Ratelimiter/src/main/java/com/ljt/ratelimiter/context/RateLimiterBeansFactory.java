package com.ljt.ratelimiter.context;

import com.ljt.ratelimiter.env.RateLimiterConfig;
import com.ljt.ratelimiter.extension.ExtensionLoader;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptor;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptorChain;
import com.ljt.ratelimiter.redis.DefaultJedisTaskExecutor;
import com.ljt.ratelimiter.redis.JedisTaskExecutor;
import com.ljt.ratelimiter.rule.alg.RateLimitRule;
import com.ljt.ratelimiter.rule.alg.UrlRateLimitRule;
import com.ljt.ratelimiter.rule.parser.JsonRuleConfigParser;
import com.ljt.ratelimiter.rule.parser.RuleConfigParser;
import com.ljt.ratelimiter.rule.parser.YamlRuleConfigParser;
import com.ljt.ratelimiter.rule.source.FileRuleConfigSource;
import com.ljt.ratelimiter.rule.source.RuleConfigSource;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Collections;
import java.util.List;

/**
 * Bean工厂创建Bean
 */
public class RateLimiterBeansFactory {
    public static final RateLimiterBeansFactory BEANS_CONTEXT = new RateLimiterBeansFactory();

    private RateLimiterBeansFactory() {
        //加载PropertySource，设置Redis和Zookeeper的相关属性
        RateLimiterConfig.instance().load();
    }

    /**
     * 获得拦截器链
     */
    public RateLimiterInterceptorChain obtainInterceptorChain(RateLimiterInterceptorChain chain) {
        if (chain == null) {
            chain = new RateLimiterInterceptorChain();
            List<RateLimiterInterceptor> interceptors = obtainLimiterInterceptors(null);
            chain.addInterceptors(interceptors);
        }
        return chain;
    }

    /**
     * 获得拦截器列表
     */
    public List<RateLimiterInterceptor> obtainLimiterInterceptors(List<RateLimiterInterceptor> interceptors) {
        //SPI
        if (interceptors == null) {
            interceptors = ExtensionLoader.getExtensionList(RateLimiterInterceptor.class);
        }

        if (interceptors == null) {
            interceptors = Collections.emptyList();
        }
        return interceptors;
    }

    /**
     * 获取{@link RuleConfigSource}
     */
    public RuleConfigSource obtainRuleConfigSource(RuleConfigSource ruleConfigSource) {
        //因为没设置spi中接口的实现类，所以这里一定为null
        if (ruleConfigSource == null) {
            ruleConfigSource = ExtensionLoader.getExtension(RuleConfigSource.class);
        }

        if (ruleConfigSource == null) {
            String sourceType = RateLimiterConfig.instance().getRuleConfigSourceType();
            if (sourceType.equals("zookeeper")) {
                //ruleConfigSource = new ZookeeperRuleConfigSource();
            } else if (sourceType.equals("file")) {
                ruleConfigSource = new FileRuleConfigSource();
            }
        }

        if (ruleConfigSource == null) {
            ruleConfigSource = new FileRuleConfigSource();
        }
        return ruleConfigSource;
    }

    /**
     * 获取RuleConfigParser
     */
    public RuleConfigParser obtainRuleConfigParser(RuleConfigParser ruleConfigParser) {
        /* create according to SPI */
        if (ruleConfigParser == null) {
            ruleConfigParser = ExtensionLoader.getExtension(RuleConfigParser.class, false);
        }

        /* create according to configuration */
        if (ruleConfigParser == null) {
            String parserType = RateLimiterConfig.instance().getRuleConfigParserType();
            if (parserType.equals("yaml")) {
                ruleConfigParser = new YamlRuleConfigParser();
            } else if (parserType.equals("json")) {
                ruleConfigParser = new JsonRuleConfigParser();
            }
        }

        /* use default rule config source. */
        if (ruleConfigParser == null) {
            ruleConfigParser = new YamlRuleConfigParser();
        }

        return ruleConfigParser;
    }

    public JedisTaskExecutor obtainJedisTaskExecutor(JedisTaskExecutor jedisTaskExecutor) {
        if (jedisTaskExecutor != null) {
            return jedisTaskExecutor;
        }
        GenericObjectPoolConfig poolConfig =
                RateLimiterConfig.instance().getRedisConfig().getPoolConfig();
        String address = RateLimiterConfig.instance().getRedisConfig().getAddress();
        int timeout = RateLimiterConfig.instance().getRedisConfig().getTimeout();
        return new DefaultJedisTaskExecutor(address, timeout, poolConfig);
    }

    public RateLimitRule obtainUrlRateLimitRule(RateLimitRule urlRateLimitRule) {
        if (urlRateLimitRule != null) {
            return urlRateLimitRule;
        }

        urlRateLimitRule = new UrlRateLimitRule();
        return urlRateLimitRule;
    }
}
