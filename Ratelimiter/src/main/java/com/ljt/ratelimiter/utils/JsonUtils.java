package com.ljt.ratelimiter.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ljt.ratelimiter.exception.ConfigurationResolveException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);

    //使用默认方式将对象序列化
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ConfigurationResolveException("parse json failed.", e);
        }
    }

    //自定义序列化器将指定类型的对象序列化
    public static <T> String toJsonString(Object object, Class<T> clazz, JsonSerializer<T> jsonSerializer) {
        try {
            objectMapper.registerModule(new SimpleModule().addSerializer(clazz, jsonSerializer));
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ConfigurationResolveException("parse json failed.", e);
        }
    }

    //json转List
    //objectMapper.getTypeFactory().constructType(clazz) 构建泛型对象
    //objectMapper.getTypeFactory().constructCollectionType(List.class, clazz) 构建泛型集合
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new ConfigurationResolveException("parse json failed.", e);
        }
    }

    //json转对象
    public static <T> T json2Object(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new ConfigurationResolveException("parse json failed.", e);
        }
    }

    //流转对象
    public static <T> T stream2Object(InputStream in, Class<T> clazz) {
        if (in == null) {
            return null;
        }

        try {
            return objectMapper.readValue(in, clazz);
        } catch (IOException e) {
            throw new ConfigurationResolveException("parse json failed.", e);
        }
    }
}
