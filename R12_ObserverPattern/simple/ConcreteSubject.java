package simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConcreteSubject implements Subject{
    //存储被注册的观察者
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }

        //异步阻塞通知实现
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Observer observer : observers) {
            futures.add(CompletableFuture.runAsync(() -> observer.update(message)));
        }

        //收集所有的CompletableFuture对象作为一个新的CompletableFuture
        //join()阻塞当前线程，直到所有的CompletableFuture对象完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
