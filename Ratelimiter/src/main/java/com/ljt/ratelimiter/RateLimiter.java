package com.ljt.ratelimiter;
import com.ljt.ratelimiter.alg.RateLimitAlg;
import com.ljt.ratelimiter.exception.InternalErrorException;
import com.ljt.ratelimiter.rule.ApiLimit;
import com.ljt.ratelimiter.rule.RateLimitRule;
import com.ljt.ratelimiter.rule.RuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 串联整个限流流程
 */
public class RateLimiter {
    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    //每个appid+api都有一个流量计数器
    private ConcurrentHashMap<String, RateLimitAlg> counters = new ConcurrentHashMap<>();

    //yaml配置规则
    private RateLimitRule rule;

    public RateLimiter() {
        //将限流规则配置文件ratelimiter-rule.yaml中的内容读取到RuleConfig中
        InputStream in = null;
        RuleConfig ruleConfig = null;

        try {
            in = Integer.class.getResourceAsStream("/ratelimiter-rule.yaml");
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, RuleConfig.class);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("close file error:{}", e);
                }
            }
        }

        this.rule = new RateLimitRule(ruleConfig);
    }

    /**
     * 根据appid与url判断是否执行限制功能
     * @param appId
     * @param url
     * @return
     */
    public boolean limit(String appId, String url) throws InternalErrorException {
        ApiLimit apiLimit = this.rule.getLimit(appId, url);
        if (apiLimit == null) {
            return true;
        }

        // 获取api对应在内存中的限流计数器（rateLimitCounter）
        String counterKey = appId + ":" + apiLimit.getApi();
        RateLimitAlg rateLimitCounter = counters.get(counterKey);
        if (rateLimitCounter == null) {
            RateLimitAlg newRateLimitCounter = new RateLimitAlg(apiLimit.getLimit());
            //使用putIfAbsent保证线程安全，不存在旧值，则rateLimitCounter=null，否则，其不为null
            rateLimitCounter = counters.putIfAbsent(counterKey, newRateLimitCounter);
            if (rateLimitCounter == null) {
                rateLimitCounter = newRateLimitCounter;
            }
        }

        return rateLimitCounter.tryAcquire();
    }
}
