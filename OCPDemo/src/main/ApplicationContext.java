package src.main;

import src.main.handler.ErrorAlertHandler;
import src.main.handler.TimeoutAlertHandler;
import src.main.handler.TpsAlertHandler;

public class ApplicationContext {
    private Alert alert;

    public void initializeBeans() {
        AlertRule rule = new AlertRule();
        Notification notification = new Notification();
        alert = new Alert();
        alert.addAlertHandler(new TimeoutAlertHandler(rule, notification));
        alert.addAlertHandler(new ErrorAlertHandler(rule, notification));
        alert.addAlertHandler(new TpsAlertHandler(rule, notification));
    }

    public Alert getAlert() {
        return alert;
    }

    // 饿汉式单例
    private static final ApplicationContext instance = new ApplicationContext();
    private ApplicationContext() {
        initializeBeans();
    }

    public static ApplicationContext getInstance() {
        return instance;
    }
}
