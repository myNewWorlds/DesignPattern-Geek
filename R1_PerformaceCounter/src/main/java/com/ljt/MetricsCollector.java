package com.ljt;

import cn.hutool.core.util.StrUtil;
import com.ljt.inner.MetricsStorage;

/**
 * 采集接口请求的原始数据。
 */
public class MetricsCollector {
    private final MetricsStorage metricsStorage;

    public MetricsCollector() {
        this(new RedisMetricsStorage());
    }

    public MetricsCollector(MetricsStorage metricsStorage) {
        this.metricsStorage = metricsStorage;
    }

    public void recordRequest(RequestInfo requestInfo) {
        if (requestInfo == null || StrUtil.isBlank(requestInfo.getApiName())) {
            return;
        }
        metricsStorage.saveRequestInfo(requestInfo);
    }
}
