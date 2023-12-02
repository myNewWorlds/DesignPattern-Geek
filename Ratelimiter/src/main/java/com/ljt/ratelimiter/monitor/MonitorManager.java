package com.ljt.ratelimiter.monitor;

import com.ljt.ratelimiter.env.RateLimiterConfig;
import com.ljt.ratelimiter.rule.ApiLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 这个类是一个监视器，收集{@link com.ljt.ratelimiter.UrlRateLimiter#limit(String, String)}
 * 关键性能指标{@link MetricType}生成统计报告
 */
public class MonitorManager {

    //性能指标统计周期
    public static final int STATISTIC_PERIOD = 60;

    private static final Logger log = LoggerFactory.getLogger(MonitorManager.class);

    //指定唯一的计数器
    private static final MetricsCounter metricsCounter = new MetricsCounter();

    //定时任务执行池，单线程，保证数据的一致性（？但在MetricsCounter中已经使用LongAdder保证线程安全了啊？）
    private static final ScheduledExecutorService scheduledExecutor =
            //创建一个容量为1（？）的线程池
            Executors.newScheduledThreadPool(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    //r为任务执行的具体内容
                    //创建一个ratelimiter-monitor-thread名字的线程
                    return new Thread(r, "ratelimiter-monitor-thread");
                }
            });

    static {
        RateLimiterConfig.instance().load();
        //设置定时任务内容
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    writeLog();
                } catch (Exception e) {
                    log.error("write log error:", e);
                }
            }
        }, STATISTIC_PERIOD, STATISTIC_PERIOD, TimeUnit.SECONDS);

        //jvm关闭时，调用钩子关闭线程池
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (!scheduledExecutor.isShutdown()) {
                    scheduledExecutor.shutdown();
                }
            }
        }));
    }

    //重置所有计数器，并打印日志
    public static void writeLog() {
        DecimalFormat mbFormat = new DecimalFormat("0.00");
        long total = metricsCounter.sumAndReset(MetricType.TOTAL);
        if (total == 0) {
            return;
        }
        long passed = metricsCounter.sumAndReset(MetricType.PASSED);
        long limited = metricsCounter.sumAndReset(MetricType.LIMITED);
        long exception = metricsCounter.sumAndReset(MetricType.EXCEPTION);
        float duration = metricsCounter.sumAndReset(MetricType.DURATION) / 1000f; // ms
        float avgDuration = duration / total;
        log.info(
                "[ratelimiter statistics] total:{}, passed:{}, limited:{}, exception:{}, avg duration:{}",
                total, passed, limited, exception, mbFormat.format(avgDuration));
    }

    public static void collect(String appId, String url, ApiLimit apiLimit, long duration, boolean result, Exception ex) {
        //总数加一
        metricsCounter.increment(MetricType.TOTAL);
        if (result) {
            metricsCounter.increment(MetricType.PASSED);
        } else {
            metricsCounter.increment(MetricType.LIMITED);
        }

        if (ex != null) {
            metricsCounter.increment(MetricType.EXCEPTION);
        }

        metricsCounter.add(MetricType.DURATION, duration);
    }
}
