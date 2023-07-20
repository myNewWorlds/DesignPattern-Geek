package src.main.handler;

import src.main.AlertRule;
import src.main.ApiStatInfo;
import src.main.Notification;

public class TimeoutAlertHandler extends AlertHandler{
    public TimeoutAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (rule.isTimeout()) {
            notification.notice("TimeoutAlertHandler");
        }
    }
}
