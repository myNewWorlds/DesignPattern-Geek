package src.main.handler;

import src.main.AlertRule;
import src.main.ApiStatInfo;
import src.main.Notification;

public class TpsAlertHandler extends AlertHandler{
    public TpsAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (rule.isTps()) {
            System.out.println("TpsAlertHandler");
        }
    }
}
