package CompletableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

    static Double fetchPrice() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (Math.random() < 0.3f) {
            throw new RuntimeException("fetch failed");
        }
        return 5 + Math.random() * 20;
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        //创建异步任务，提交给线程池
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice, es);

        //执行成功
        future.thenAccept(result -> System.out.println("prince" + result));

        //执行异常
        future.exceptionally(e -> {
            System.out.println(e.getMessage());
            return null;
        });

        es.shutdown();
    }
}













