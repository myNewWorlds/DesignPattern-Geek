package simple;

/**
 * 观察者处理的接口类
 * 添加观察者、移除观察者、触发观察者事件
 */
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(String message);
}
