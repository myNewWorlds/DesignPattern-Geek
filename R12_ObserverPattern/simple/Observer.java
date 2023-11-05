package simple;

/**
 * 观察者接口类
 */
public interface Observer {
    //接收通知时的更新程序
    void update(String message);
}
