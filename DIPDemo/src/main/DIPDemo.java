package src.main;

/**
 * 依赖注入
 */
public class DIPDemo {
    public static void main(String[] args) {
        DIPNotification notification = new DIPNotification(new SmsSender());
        notification.sendMessage("a", "b");
    }
}

class DIPNotification {
    private final DIPMessageSender dipMessageSender;

    public DIPNotification(DIPMessageSender dipMessageSender) {
        this.dipMessageSender = dipMessageSender;
    }

    public void sendMessage(String cellPhone, String message) {
        dipMessageSender.send(cellPhone, message);
    }
}

interface DIPMessageSender {
    void send(String cellphone, String message);
}

//短信发送类
class SmsSender implements DIPMessageSender {
    @Override
    public void send(String cellphone, String message) {
        System.out.println("短信");
    }
}

//站内信息发送
class InboxSender implements DIPMessageSender {
    @Override
    public void send(String cellphone, String message) {
        System.out.println("讯息");
    }
}
