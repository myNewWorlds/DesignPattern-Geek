package com.ljt.ratelimiter.env;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加载和管理配置属性
 * 各种配置文件的解析结果存放类
 */
@Getter
public class PropertySource {

    private final Map<String, Object> properties = new LinkedHashMap<>();

    public PropertySource() {
    }

    public PropertySource(Map<String, Object> properties) {
        addProperties(properties);
    }

    public void addProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    //合并其他属性源
    public void combinePropertySource(PropertySource propertySource) {
        if (propertySource == null || propertySource.getProperties().isEmpty()) {
            return;
        }
        addProperties(propertySource.getProperties());
    }

    public boolean containsKey(String name) {
        return properties.containsKey(name);
    }

    public Object getPropertyValue(String name) {
        return properties.get(name);
    }

    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[0]);
    }

    public String getPropertyStringValue(String name) {
        Object oval = properties.get(name);
        if (oval == null) {
            return null;
        }

        if (oval instanceof String) {
            return (String) oval;
        }

        return String.valueOf(oval);
    }

    public Integer getPropertyIntValue(String name) {
        Object oval = properties.get(name);
        if (oval == null) {
            return null;
        }

        if (oval instanceof Integer) {
            return (Integer) oval;
        }
        try {
            return Integer.valueOf(getPropertyStringValue(name));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getPropertyBooleanValue(String name) {
        Object oval = properties.get(name);
        if (oval == null) {
            return null;
        }

        if (oval instanceof Boolean) {
            return (Boolean) oval;
        }

        if ("true".equalsIgnoreCase(getPropertyStringValue(name))) {
            return Boolean.TRUE;
        }

        if ("false".equalsIgnoreCase(getPropertyStringValue(name))) {
            return Boolean.FALSE;
        }
        return null;
    }

}
