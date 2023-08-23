import java.util.Random;

/**
 * 性能计数器 计算用户的登录和注册时间
 */
public class UserController {
    private final Metrics metrics;

    public UserController(Metrics metrics) {
        this.metrics = metrics;
    }

    //注册
    public void register() throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        metrics.recordTimestamp("register", startTimestamp);
        Thread.sleep(new Random().nextInt(10));
        long respTime = System.currentTimeMillis() - startTimestamp;
        metrics.recordResponseTime("register", respTime);
    }

    public void login() throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        metrics.recordTimestamp("login", startTimestamp);
        Thread.sleep(new Random().nextInt(10));
        long respTime = System.currentTimeMillis() - startTimestamp;
        metrics.recordResponseTime("login", respTime);
    }
}
