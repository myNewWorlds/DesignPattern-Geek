package com.ljt.ratelimiter.env.loader;

import com.ljt.ratelimiter.env.PropertySource;

/**
 * 环境变量加载器的抽象类
 * 解析{@link com.ljt.ratelimiter.env.io.Resource}到{@link PropertySource}
 */
public interface PropertySourceLoader {
    PropertySource load();
}
