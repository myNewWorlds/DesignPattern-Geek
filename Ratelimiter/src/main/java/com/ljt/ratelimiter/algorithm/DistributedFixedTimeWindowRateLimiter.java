package com.ljt.ratelimiter.algorithm;

import com.ljt.ratelimiter.exception.InternalErrorException;
import com.ljt.ratelimiter.redis.JedisTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

/**
 * 基于redis的固定窗口算法
 */
public class DistributedFixedTimeWindowRateLimiter implements RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(DistributedFixedTimeWindowRateLimiter.class);

    //流量限制的key （因为每个key一个此计数器，所以key和limit都设置为final）
    private final String key;
    //流量限制的最大值
    private final int limit;
    //The Jedis wrapper to access Redis
    private final JedisTaskExecutor jedisTaskExecutor;

    //lua脚本 TPS:limit/1s KEYS[1]=key,ARGV[1]=limit,return=result
    public static final String REDIS_LIMIT_SCRIPT =
            "local key = KEYS[1] " +
            "local limit = tonumber(ARGV[1]) " +
            "local current = tonumber(redis.call('incr', key)) " +
            "if current > limit then " +  //current > limit 说明超过限制，返回0
            "   return 0 " +
            "elseif current == 1 then " + //current == 1 说明第一次计数，设置过期时间为1秒
            "   redis.call('expire', key, '1') " +
            "end " +
            "return 1 ";
    //得到脚本的sha1值
    public static final String REDIS_LIMIT_SCRIPT_SHA1 = sha1Hex(REDIS_LIMIT_SCRIPT);

    public DistributedFixedTimeWindowRateLimiter(String key, int limit, JedisTaskExecutor jedisTaskExecutor) {
        this.key = key;
        this.limit = limit;
        this.jedisTaskExecutor = jedisTaskExecutor;
    }

    /**
     * 尝试得到一个访问令牌
     */
    @Override
    public boolean tryAcquire() throws InternalErrorException {
        long result = 0;
        try {
            result = (long) jedisTaskExecutor.evalsha(REDIS_LIMIT_SCRIPT_SHA1, key, String.valueOf(limit));
            return result == 1;
        } catch (JedisNoScriptException e) {
            log.warn("no lua script cache on redis server", e);
        } catch (JedisException e) {
            throw new InternalErrorException("Read redis error.", e);
        }

        try {
            result = (long) jedisTaskExecutor.eval(REDIS_LIMIT_SCRIPT, key, String.valueOf(limit));
        } catch (JedisConnectionException e) {
            throw new InternalErrorException("Read redis error.", e);
        }
        return 1 == result;
    }
}
