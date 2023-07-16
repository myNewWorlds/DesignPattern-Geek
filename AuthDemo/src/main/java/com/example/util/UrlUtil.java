package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class UrlUtil {
    public static class UrlEntity{
        //基础url
        public String baseUrl;
        //url参数
        public Map<String,String> params;

        public String getParams(String key) {
            return params.get(key);
        }
    }

    /**
     * 解析url
     */
    public static UrlEntity parse(String url) {
        UrlEntity urlEntity = new UrlEntity();
        if (url == null) {
            return urlEntity;
        }
        url = url.trim();
        if (url.equals("")) {
            return urlEntity;
        }
        String[] urlParts = url.split("\\?");
        urlEntity.baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return urlEntity;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        urlEntity.params = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            urlEntity.params.put(keyValue[0], keyValue[1]);
        }
        return urlEntity;
    }
}
