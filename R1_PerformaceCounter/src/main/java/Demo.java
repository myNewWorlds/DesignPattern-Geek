import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        Metrics metrics = new Metrics();
        metrics.startRepeatedReport(2, TimeUnit.SECONDS);
        UserController user = new UserController(metrics);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(2 * 1000);
            user.register();
            user.login();
        }
    }
}
