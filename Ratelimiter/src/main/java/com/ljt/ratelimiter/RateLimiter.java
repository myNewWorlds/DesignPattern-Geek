package com.ljt.ratelimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 串联整个限流流程
 */
public class RateLimiter {
    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    //每个appid+api都有一个流量计数器
    //private ConcurrentHashMap<String, RateLimitAlg> counters = new ConcurrentHashMap<>();



    public RateLimiter() {
        //FileRuleConfigSource
    }

    /**
     * 根据appid与url判断是否执行限制功能
     * @param appId
     * @param url
     * @return
     */
//    public boolean limit(String appId, String url) throws InternalErrorException {
//        ApiLimit apiLimit = this.rule.getLimit(appId, url);
//        if (apiLimit == null) {
//            return true;
//        }
//
//        // 获取api对应在内存中的限流计数器（rateLimitCounter）
//        String counterKey = appId + ":" + apiLimit.getApi();
//        RateLimitAlg rateLimitCounter = counters.get(counterKey);
//        if (rateLimitCounter == null) {
//            RateLimitAlg newRateLimitCounter = new RateLimitAlg(apiLimit.getLimit());
//            //使用putIfAbsent保证线程安全，不存在旧值，则rateLimitCounter=null，否则，其不为null
//            rateLimitCounter = counters.putIfAbsent(counterKey, newRateLimitCounter);
//            if (rateLimitCounter == null) {
//                rateLimitCounter = newRateLimitCounter;
//            }
//        }
//
//        return rateLimitCounter.tryAcquire();
//    }
}
