package com.ljt.ratelimiter.redis;

import com.ljt.ratelimiter.env.RedisConfig;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * 默认jedis连接池
 * 如果要设置连接池属性，在{@link RedisConfig}中进行设置连接池属性
 * (将该类移入{@link RedisConfig} 是否也可以)
 */
public class DefaultJedisPoolConfig extends JedisPoolConfig {

    private static final int DEFAULT_MAX_TOTAL = 50;
    private static final int DEFAULT_MAX_IDLE = 50;
    private static final int DEFAULT_MIN_IDLE = 20;
    private static final long DEFAULT_MAX_WAIT_MILLIS = 10;
    private static final boolean DEFAULT_TEST_ON_BORROW = true;

    public DefaultJedisPoolConfig() {
        super();
        setMaxTotal(DEFAULT_MAX_TOTAL);
        setMaxIdle(DEFAULT_MAX_IDLE);
        setMinIdle(DEFAULT_MIN_IDLE);
        setMaxWait(Duration.ofMillis(DEFAULT_MAX_WAIT_MILLIS));
        setTestOnBorrow(DEFAULT_TEST_ON_BORROW);
    }
}
