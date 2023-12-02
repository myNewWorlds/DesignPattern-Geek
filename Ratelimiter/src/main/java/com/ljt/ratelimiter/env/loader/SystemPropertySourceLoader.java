package com.ljt.ratelimiter.env.loader;

import com.ljt.ratelimiter.env.PropertyConstants;
import com.ljt.ratelimiter.env.PropertySource;
import com.ljt.ratelimiter.extension.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作系统的环境变量
 */
@Order(Order.HIGHEST_PRECEDENCE + 20)
public class SystemPropertySourceLoader implements PropertySourceLoader {

    @Override
    public PropertySource load() {
        Map<String, Object> ratelimiterProperties = new HashMap<String, Object>();
        Map<String, String> envs = getEnv();
        for (Map.Entry<String, String> env : envs.entrySet()) {
            if (env.getKey().startsWith(PropertyConstants.PROPERTY_KEY_PREFIX)) {
                ratelimiterProperties.put(env.getKey(), env.getValue());
            }
        }
        PropertySource source = new PropertySource();
        source.addProperties(ratelimiterProperties);
        return source;
    }

    protected Map<String, String> getEnv() {
        return System.getenv();
    }
}
