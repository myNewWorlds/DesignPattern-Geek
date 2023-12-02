package com.ljt.ratelimiter.env.loader;

import com.ljt.ratelimiter.env.PropertyConstants;
import com.ljt.ratelimiter.env.PropertySource;
import com.ljt.ratelimiter.extension.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * jvm中默认环境变量
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
public class JvmPropertySourceLoader implements PropertySourceLoader{

    @Override
    public PropertySource load() {
        Properties properties = System.getProperties();
        Map<String, Object> ratelimiterProperties = new HashMap<>();
        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            if (name.startsWith(PropertyConstants.PROPERTY_KEY_PREFIX)) {
                ratelimiterProperties.put(name, properties.get(name));
            }
        }
        PropertySource source = new PropertySource();
        source.addProperties(ratelimiterProperties);
        return source;
    }
}
