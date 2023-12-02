package com.ljt.ratelimiter.monitor;

/**
 * 用于{@link MonitorManager}统计的关键性能指标
 */
public enum MetricType {
    TOTAL, // total count of limit operations url请求总数
    PASSED, // get an access token  允许访问的url数量
    LIMITED, // overload 不允许访问的url数量
    TIMEOUT, // limit operation timeout
    EXCEPTION, // some internal error occurs 访问的url出现异常的数量
    DURATION // cost time of limit operation url访问花费的时间
}
