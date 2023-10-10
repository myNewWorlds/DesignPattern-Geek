package com.ljt;

import com.google.gson.Gson;
import com.ljt.inner.MetricsStorage;
import com.ljt.inner.StatViewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 将统计信息打印到控制台
 */
public class ConsoleReporter {
    private MetricsStorage metricsStorage;
    private Aggregator aggregator;
    private StatViewer viewer;
    private ScheduledExecutorService executor;

    public ConsoleReporter(MetricsStorage metricsStorage, Aggregator aggregator, StatViewer viewer) {
        this.metricsStorage = metricsStorage;
        this.aggregator = aggregator;
        this.viewer = viewer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    //定时向控制台打印统计信息
    public void startRepeatReport(long periodInSeconds, long durationInSeconds) {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 第1个代码逻辑：根据给定的时间区间，从数据库中拉取数据；
                long durationInMillis = durationInSeconds * 1000;
                long endTimeInMillis = System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                Map<String, List<RequestInfo>> requestInfos = metricsStorage.getRequestInfos(startTimeInMillis, endTimeInMillis);
                // 第2个代码逻辑：根据原始数据，计算得到统计数据；
                Map<String, RequestStat> requestStats = aggregator.aggtregator(requestInfos, durationInMillis);
                // 第3个代码逻辑：将统计数据显示到终端（命令行或邮件）；
                viewer.output(requestStats, startTimeInMillis, endTimeInMillis);
            }
        }, 0, periodInSeconds, TimeUnit.SECONDS);
    }
}
