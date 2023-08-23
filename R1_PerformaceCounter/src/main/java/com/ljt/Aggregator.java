package com.ljt;

import java.util.List;

/**
 * 根据原始数据计算统计数据
 */
public class Aggregator {
    /**
     * 统计requestInfos的最大值，最小值，响应次数等
     *
     * @param requestInfos      进行统计的数据
     * @param durationInSeconds 在durationInSeconds时间段内得到requestInfos数据
     * @return RequestStat 统计结果
     */
    public static RequestStat aggregate(List<RequestInfo> requestInfos, long durationInSeconds) {
        double maxRespTime = Double.MAX_VALUE;
        double minRespTime = Double.MIN_VALUE;
        double avgRespTime = -1;
        double p999RespTime = -1;
        double p99RespTime = -1;
        double sumRespTime = 0;
        long count = 0;

        for (RequestInfo requestInfo : requestInfos) {
            count++;
            double responseTime = requestInfo.getResponseTime();
            maxRespTime = Math.max(maxRespTime, responseTime);
            minRespTime = Math.min(minRespTime, responseTime);
            sumRespTime += responseTime;
        }
        if (count != 0) {
            avgRespTime = sumRespTime / count;
        }
        long tps = count / durationInSeconds * 1000;
        requestInfos.sort((o1, o2) -> (int) (o1.getResponseTime() - o2.getResponseTime()));
        int idx999 = (int) (count * 0.999);
        int idx99 = (int) (count * 0.99);
        if (count != 0) {
            p999RespTime = requestInfos.get(idx999).getResponseTime();
            p99RespTime = requestInfos.get(idx99).getResponseTime();
        }
        RequestStat requestStat = new RequestStat();
        requestStat.setMaxResponseTime(maxRespTime);
        requestStat.setMinResponseTime(minRespTime);
        requestStat.setAvgResponseTime(avgRespTime);
        requestStat.setP999ResponseTime(p999RespTime);
        requestStat.setP99ResponseTime(p99RespTime);
        requestStat.setCount(count);
        requestStat.setTps(tps);
        return requestStat;
    }
}
