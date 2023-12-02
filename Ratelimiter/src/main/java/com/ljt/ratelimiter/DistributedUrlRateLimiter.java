package com.ljt.ratelimiter;

import com.ljt.ratelimiter.algorithm.DistributedFixedTimeWindowRateLimiter;
import com.ljt.ratelimiter.algorithm.RateLimiter;
import com.ljt.ratelimiter.env.RedisConfig;
import com.ljt.ratelimiter.env.ZookeeperConfig;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptor;
import com.ljt.ratelimiter.interceptor.RateLimiterInterceptorChain;
import com.ljt.ratelimiter.redis.DefaultJedisTaskExecutor;
import com.ljt.ratelimiter.redis.JedisTaskExecutor;
import com.ljt.ratelimiter.rule.alg.RateLimitRule;
import com.ljt.ratelimiter.rule.parser.JsonRuleConfigParser;
import com.ljt.ratelimiter.rule.parser.RuleConfigParser;
import com.ljt.ratelimiter.rule.parser.YamlRuleConfigParser;
import com.ljt.ratelimiter.rule.source.FileRuleConfigSource;
import com.ljt.ratelimiter.rule.source.RuleConfigSource;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.ljt.ratelimiter.context.RateLimiterBeansFactory.BEANS_CONTEXT;

/**
 * 基于Redis的固定窗口算法
 */
public class DistributedUrlRateLimiter extends AbstractUrlRateLimiter implements UrlRateLimiter {

    //Jedis执行器
    private JedisTaskExecutor jedisTaskExecutor;

    public DistributedUrlRateLimiter() {
        this(null, (RateLimitRule) null);
    }
    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor) {
        this(jedisTaskExecutor, (RateLimitRule) null);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RuleConfigSource source) {
        super(source);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RateLimitRule rule) {
        super(rule);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RateLimitRule rule,
                                     RateLimiterInterceptorChain chain) {
        super(rule, chain);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    @Override
    protected RateLimiter createRateLimitAlgorithm(String limitKey, int limit) {
        return new DistributedFixedTimeWindowRateLimiter(limitKey, limit, jedisTaskExecutor);
    }

    //建造者模式
    public static DistributedUrlRateLimiterbuilder builder = new DistributedUrlRateLimiterbuilder();
    public static class DistributedUrlRateLimiterbuilder{
        /* redis configuratoin */
        private RedisConfig redisConfig;

        /* zookeeper configuration */
        private ZookeeperConfig zookeeperConfig;

        /* interceptors */
        private List<RateLimiterInterceptor> interceptors;

        /* rule configuration parser: yaml or json */
        private String ruleParserType = "yaml";

        /* source type: file or zookeeper */
        private String ruleSourceType = "file";

        public DistributedUrlRateLimiterbuilder() {}

        public DistributedUrlRateLimiterbuilder setRedisConfig(RedisConfig redisConfig) {
            this.redisConfig = redisConfig;
            return this;
        }

        public DistributedUrlRateLimiterbuilder setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
            this.zookeeperConfig = zookeeperConfig;
            return this;
        }

        public DistributedUrlRateLimiterbuilder setInterceptors(
                List<RateLimiterInterceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public DistributedUrlRateLimiterbuilder setRuleParserType(String ruleParserType) {
            if (StringUtils.isNotBlank(ruleParserType)) {
                this.ruleParserType = ruleParserType;
            }
            return this;
        }

        public DistributedUrlRateLimiterbuilder setRuleSourceType(String ruleSourceType) {
            if (StringUtils.isNotBlank(ruleSourceType)) {
                this.ruleSourceType = ruleSourceType;
            }
            return this;
        }

        public DistributedUrlRateLimiter build() {
            JedisTaskExecutor executor = new DefaultJedisTaskExecutor(redisConfig.getAddress(),
                    redisConfig.getTimeout(), redisConfig.getPoolConfig());

            RuleConfigParser parser = null;
            if (this.ruleParserType.equals("yaml")) {
                parser = new YamlRuleConfigParser();
            } else if (this.ruleParserType.equals("json")) {
                parser = new JsonRuleConfigParser();
            } else {
                throw new RuntimeException("Do not support the rule paser type: " + this.ruleParserType);
            }

            RuleConfigSource source = null;
            if (this.ruleSourceType.equals("file")) {
                source = new FileRuleConfigSource();
            } else if (this.ruleSourceType.equals("zookeeper")) {
                if (zookeeperConfig != null && StringUtils.isNoneBlank(zookeeperConfig.getAddress())
                        && StringUtils.isNoneBlank(zookeeperConfig.getPath())) {
                    //source = new ZookeeperRuleConfigSource(zookeeperConfig.getAddress(), zookeeperConfig.getPath(), parser);
                } else {
                    throw new RuntimeException("some zookeeper configuration is empty.");
                }
            } else {
                throw new RuntimeException("Do not support the rule source type: " + this.ruleSourceType);
            }

            DistributedUrlRateLimiter ratelimiter = new DistributedUrlRateLimiter(executor, source);
            if (this.interceptors != null && !this.interceptors.isEmpty()) {
                ratelimiter.addInterceptors(interceptors);
            }
            return ratelimiter;
        }
    }
}
