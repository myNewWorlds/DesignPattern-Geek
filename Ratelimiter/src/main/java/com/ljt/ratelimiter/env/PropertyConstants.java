package com.ljt.ratelimiter.env;

/**
 * 环境配置属性的常量名
 */
public class PropertyConstants {
    //整个框架的属性前缀
    public static final String PROPERTY_KEY_PREFIX = "ratelimiter";

    /* Redis config if needed. */
    public static final String PROPERTY_REDIS_ADDRESS = PROPERTY_KEY_PREFIX + ".redis.address";
    public static final String PROPERTY_REDIS_MAX_TOTAL = PROPERTY_KEY_PREFIX + ".redis.maxTotal";
    public static final String PROPERTY_REDIS_MAX_IDLE = PROPERTY_KEY_PREFIX + ".redis.maxIdle";
    public static final String PROPERTY_REDIS_MIN_IDLE = PROPERTY_KEY_PREFIX + ".redis.minIdle";
    public static final String PROPERTY_REDIS_MAX_WAIT_MILLIS =
            PROPERTY_KEY_PREFIX + ".redis.maxWaitMillis";
    public static final String PROPERTY_REDIS_TEST_ON_BORROW =
            PROPERTY_KEY_PREFIX + ".redis.testOnBorrow";
    public static final String PROPERTY_REDIS_TIMEOUT = PROPERTY_KEY_PREFIX + ".redis.timeout";
}
