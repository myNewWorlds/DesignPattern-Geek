package com.ljt.ratelimiter.redis;

/**
 * 访问redis的Jedis封装器
 * The Jedis wrapper used to access Redis.
 */
public interface JedisTaskExecutor {

    Object eval(String luaScript);

    Object eval(String luaScript, String key, String params);

    Object evalsha(String sha1, String key, String params);

    String set(String key, String value);
}
