package com.ljt.ratelimiter.env.resolver;

import com.ljt.ratelimiter.exception.ConfigurationResolveException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 解析yaml文件
 */
public class YamlPropertySourceResolver extends AbstractPropertySourceResolver implements PropertySourceResolver {

    public YamlPropertySourceResolver() {
        super();
    }

    @Override
    public String[] getSupportedFileExtensions() {
        return new String[]{"yaml", "yml"};
    }

    @Override
    public Map<String, Object> resolve(InputStream in) throws ConfigurationResolveException {
        Yaml yaml = new Yaml();
        Map<String, Object> flattenedMap = new HashMap<>();
        try (Reader reader = new UnicodeReader(in)) {
            for (Object object : yaml.loadAll(reader)) {
                if (object != null) {
                    flattenedMap.putAll(getFlattenedMap(asMap(object)));
                }
            }
        } catch (Exception e) {
            throw new ConfigurationResolveException("parse yaml configuration failed.", e);
        }
        return flattenedMap;
    }

    /**
     * 递归解析yaml配置
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (!(object instanceof Map)) {
            return Collections.emptyMap();
        }

        Map<Object, Object> map = (Map<Object, Object>) object;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = asMap(value);
            }
            Object key = entry.getKey();
            if (key instanceof CharSequence) {
                result.put(key.toString(), value);
            } else {
                result.put("[" + key.toString() + "]", value);
            }
        }
        return result;
    }

    /**
     * 将Map的层级扁平化
     */
    private Map<String, Object> getFlattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, source, null);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            if (hasText(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + "." + key;
                }
            }
            Object value = entry.getValue();
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                buildFlattenedMap(result, (Map<String, Object>) value, key);
            } else if (value instanceof Collection) {
                Collection<Object> collections = (Collection<Object>) value;
                int count = 0;
                for (Object object : collections) {
                    //真聪明的处理，以[0]、[1]等为键，collection为值构建map
                    buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
                }
            } else {
                result.put(key, value != null ? value : "");
            }
        }
    }

    private boolean hasText(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
