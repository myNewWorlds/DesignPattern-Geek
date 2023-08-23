package com.ljt.inner;

import com.ljt.RequestInfo;

import java.util.List;
import java.util.Map;

/**
 * 存储采集的数据
 */
public interface MetricsStorage {
    void saveRequestInfo(RequestInfo requestInfo);

    List<RequestInfo> getRequestInfos(String apiName, long startTimeInMillis, long endTimeInMillis);

    Map<String, List<RequestInfo>> getRequestInfos(long startTimeInMillis, long endTimeInMillis);
}
