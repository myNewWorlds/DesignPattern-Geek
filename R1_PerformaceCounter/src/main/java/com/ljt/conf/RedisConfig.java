package com.ljt.conf;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static final JedisPool pool;

    //初始化连接池
    static {
        pool = new JedisPool(new JedisPoolConfig(), "192.168.29.130", 6379, 500, "123321");
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

}
