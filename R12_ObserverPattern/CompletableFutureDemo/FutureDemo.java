package CompletableFutureDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<Integer> f = es.submit(() -> {
            try {
                Thread.sleep(10000);
                return 100;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //非阻塞式查询任务是否执行成功
        System.out.println(f.isDone());
        System.out.println("得到前");
        //阻塞式得到结果
        System.out.println(f.get());
        System.out.println("得到后");
        //不关闭线程池，主线程也不会关闭
        es.shutdown();
    }
}
