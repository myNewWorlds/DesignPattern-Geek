package com.ljt;

import com.ljt.inner.MetricsStorage;

import java.util.Random;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        MetricsStorage storage = new RedisMetricsStorage();
        Aggregator aggregator = new Aggregator();

        ConsoleViewer consoleViewer = new ConsoleViewer();
        ConsoleReporter consoleReporter = new ConsoleReporter(storage,aggregator,consoleViewer);
        consoleReporter.startRepeatReport(3, 3);

        MetricsCollector collector = new MetricsCollector(storage);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(2 * 1000);
            int value = new Random().nextInt(10);
            if (value % 2 == 0) {
                collector.recordRequest(new RequestInfo("register", value, System.currentTimeMillis()));
            } else {
                collector.recordRequest(new RequestInfo("login", value, System.currentTimeMillis()));
            }
        }
    }
}
