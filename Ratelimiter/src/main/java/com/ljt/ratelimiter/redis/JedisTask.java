package com.ljt.ratelimiter.redis;

import redis.clients.jedis.Jedis;

/**
 * 函数式接口的关键是方法体可以在使用的时候自由定义（即可将唯一的方法直接替换为自定义的方法体）。
 * 类似于模板模式，进行回调；相当于外面的框架做好了，你只需要填充内部内容即可
 * 注意：函数接口作为参数被调用时，只是实现了方法体，
 * 这里的T定义的是返回结果的类型
 */
@FunctionalInterface
public interface JedisTask<T> {
    T run(Jedis jedis);
}
