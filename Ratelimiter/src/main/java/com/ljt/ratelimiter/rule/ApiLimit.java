package com.ljt.ratelimiter.rule;

import lombok.Data;

/**
 * 单个api的限制规则
 */
@Data
public class ApiLimit {
    private static final int DEFAULT_TIME_UNIT = 1; // 1 second
    private String api;
    private int unit; //限流的时间粒度
    private int limit; //unit时间内限制的访问次数

    public ApiLimit(String api, int limit) {
        this(api,limit,DEFAULT_TIME_UNIT);
    }

    public ApiLimit(String api, int unit, int limit) {
        this.api = api;
        this.unit = unit;
        this.limit = limit;
    }
}
