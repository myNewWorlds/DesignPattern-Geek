package com.ljt.ratelimiter.redis;

import com.google.common.collect.Lists;
import com.ljt.ratelimiter.env.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Default implementation for {@link JedisTaskExecutor} which is the jedis wrapper used to acess Redis
 * 创建默认的redis执行器
 * 1. 构建
 *  * 传入自定义的连接池
 *  * 传入address与连接池配置进行构建（可以使用{@link RedisConfig}中的静态属性与{@link DefaultJedisPoolConfig}进行默认配置）
 * 2. 定义基本的lua脚本执行方法
 */
public class DefaultJedisTaskExecutor implements JedisTaskExecutor {

    private final JedisPool pool;
    private static final String DEFAULT_REDIS_KEY_PREFIX = "rt:";
    private final String redisKeyPrefix;

    //已经存在连接池
    public DefaultJedisTaskExecutor(JedisPool pool) {
        this(pool, DEFAULT_REDIS_KEY_PREFIX);
    }

    public DefaultJedisTaskExecutor(JedisPool pool, String redisKeyPrefix) {
        this.pool = pool;
        this.redisKeyPrefix = redisKeyPrefix;
    }

    //根据连接地址、连接池配置构建连接池
    public DefaultJedisTaskExecutor(String address, int timeout, GenericObjectPoolConfig poolConfig) {
        this(address, timeout, poolConfig, DEFAULT_REDIS_KEY_PREFIX);
    }

    public DefaultJedisTaskExecutor(String address, int timeout, GenericObjectPoolConfig poolConfig, String prefix) {
        if (StringUtils.isBlank(address)) {
            throw new RuntimeException("redis address is empty");
        }

        String[] ipAndPort = address.split(":");
        String host = ipAndPort[0];
        int port = RedisConfig.DEFAULT_PORT;
        if (ipAndPort.length >= 2) {
            try {
                port = Integer.parseInt(ipAndPort[1]);
            } catch (NumberFormatException e) {
                port = RedisConfig.DEFAULT_PORT;
            }
        }
        if (poolConfig == null) {
            poolConfig = new DefaultJedisPoolConfig();
        }
        this.pool = new JedisPool(poolConfig, host, port, timeout);
        this.redisKeyPrefix = prefix;
    }

    @Override
    public Object eval(String luaScript) {
        return execute(jedis -> jedis.eval(luaScript));
    }

    @Override
    public Object eval(String luaScript, String key, String params) {
        return execute(jedis -> jedis.eval(luaScript, Lists.newArrayList(redisKeyPrefix + key),
                Lists.newArrayList(params)));
    }

    @Override
    public Object evalsha(String sha1, String key, String params) {
        return execute(jedis -> jedis.evalsha(sha1, Lists.newArrayList(redisKeyPrefix + key),
                Lists.newArrayList(params)));
    }

    @Override
    public String set(String key, String value) {
        return execute(jedis -> jedis.set(redisKeyPrefix + key, value));
    }

    /**
     * 精髓
     * 1. 每次执行时，从连接池得到jedis，并通过try自动释放
     * 2. task.run(jedis)的方法体每次被调用时，都可以自定义
     * 即每次都是用 连接池得到的jedis，执行当前自己定义的方体。
     */
    private <T> T execute(JedisTask<T> task) {
        T result;
        try(Jedis jedis = pool.getResource()) {
            result = task.run(jedis);
        }
        return result;
    }

}
