package com.ljt.ratelimiter.env;

import com.ljt.ratelimiter.redis.DefaultJedisPoolConfig;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Redis配置类
 * 1. 使用buildFromProperties，通过配置文件更新
 * 2. 通过set在代码中更新
 * 3. 不更新，使用默认配置
 */
@Data
public class RedisConfig {
    //connectTimeout and soTimeout(SocketTimeout) 连接超时与读取超时
    public static final int DEFAULT_TIMEOUT = 10;

    //默认redis端口
    public static final int DEFAULT_PORT = 6379;

    private String address; //连接地址
    private int timeout = DEFAULT_TIMEOUT; //超时时间
    private GenericObjectPoolConfig poolConfig = new DefaultJedisPoolConfig(); //连接池

    public void buildFromProperties(PropertySource propertySource) {
        if (propertySource == null) {
            return;
        }

        String addr = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_REDIS_ADDRESS);
        if (StringUtils.isNotBlank(addr)) {
            this.address = addr;
        }

        Integer timeout = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_TIMEOUT);
        if (timeout != null) {
            this.timeout = timeout;
        }

        Integer maxTotal =
                propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_TOTAL);
        if (maxTotal != null) {
            this.poolConfig.setMaxTotal(maxTotal);
        }

        Integer maxIdle = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_IDLE);
        if (maxIdle != null) {
            this.poolConfig.setMaxIdle(maxIdle);
        }

        Integer minIdle = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MIN_IDLE);
        if (minIdle != null) {
            this.poolConfig.setMinIdle(minIdle);
        }

        Integer maxWaitMillis =
                propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_WAIT_MILLIS);
        if (maxWaitMillis != null) {
            this.poolConfig.setMaxWaitMillis(maxWaitMillis);
        }

        Boolean testOnBorrow =
                propertySource.getPropertyBooleanValue(PropertyConstants.PROPERTY_REDIS_TEST_ON_BORROW);
        if (testOnBorrow != null) {
            this.poolConfig.setTestOnBorrow(testOnBorrow);
        }
    }
}
