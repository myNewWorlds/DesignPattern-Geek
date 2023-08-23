package com.ljt;

import com.google.gson.Gson;
import com.ljt.conf.RedisConfig;
import com.ljt.inner.MetricsStorage;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisMetricsStorage implements MetricsStorage {

    @Override
    public void saveRequestInfo(RequestInfo requestInfo) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Gson gson = new Gson();
            jedis.zadd(requestInfo.getApiName(), requestInfo.getTimestamp(), gson.toJson(requestInfo));
        }
    }

    @Override
    public List<RequestInfo> getRequestInfos(String apiName, long startTimeInMillis, long endTimeInMillis) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Gson gson = new Gson();
            Set<String> strs = jedis.zrangeByScore(apiName, startTimeInMillis, endTimeInMillis);
            List<RequestInfo> requestInfos = new ArrayList<>();
            for (String str : strs) {
                RequestInfo requestInfo = gson.fromJson(str, RequestInfo.class);
                requestInfos.add(requestInfo);
            }
            return requestInfos;
        }
    }

    @Override
    public Map<String, List<RequestInfo>> getRequestInfos(long startTimeInMillis, long endTimeInMillis) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Gson gson = new Gson();
            Set<String> keys = jedis.keys("*");
            Map<String, List<RequestInfo>> map = new LinkedHashMap<>();
            for (String key : keys) {
                List<RequestInfo> requestInfos = new ArrayList<>();
                Set<String> strs = jedis.zrangeByScore(key, startTimeInMillis, endTimeInMillis);
                for (String str : strs) {
                    RequestInfo requestInfo = gson.fromJson(str, RequestInfo.class);
                    requestInfos.add(requestInfo);
                }
                map.put(key, requestInfos);
            }
            return map;
        }
    }
}
