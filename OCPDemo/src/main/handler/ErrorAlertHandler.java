package src.main.handler;

import src.main.AlertRule;
import src.main.ApiStatInfo;
import src.main.Notification;

public class ErrorAlertHandler extends AlertHandler{

    public ErrorAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (rule.isError()) {
            notification.notice("ErrorAlertHandler");
        }
    }
}
