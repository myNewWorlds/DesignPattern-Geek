package com.ljt.ratelimiter.env.resolver;

import com.ljt.ratelimiter.env.PropertyConstants;
import com.ljt.ratelimiter.exception.ConfigurationResolveException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesPropertySourceResolver extends AbstractPropertySourceResolver implements PropertySourceResolver{
    @Override
    public String[] getSupportedFileExtensions() {
        return new String[] { "properties" };
    }

    @Override
    public Map<String, Object> resolve(InputStream in) throws ConfigurationResolveException {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new ConfigurationResolveException("parse properties configuration failed.", e);
        }
        Map<String, Object> propertiesMap = new HashMap<>();
        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            if (name.startsWith(PropertyConstants.PROPERTY_KEY_PREFIX)) {
                propertiesMap.put(name, properties.get(name));
            }
        }
        return propertiesMap;
    }
}
