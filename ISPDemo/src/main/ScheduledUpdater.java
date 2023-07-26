package src.main;

import src.main.inter.Updater;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时调用 redisConfig 与 kafkaConfig的update方法
 */
public class ScheduledUpdater {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final long initialDelayInSeconds; //初始延迟秒数
    private final long periodInSeconds; //周期秒数
    private final Updater updater;

    public ScheduledUpdater(long initialDelayInSeconds, long periodInSeconds, Updater updater) {
        this.initialDelayInSeconds = initialDelayInSeconds;
        this.periodInSeconds = periodInSeconds;
        this.updater = updater;
    }

    public void run() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updater.update();
            }
        }, initialDelayInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }
}
