package EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 表示被@Subscribe 注解的方法
 */
public class ObserverAction {
    private Object target;
    private Method method;

    public ObserverAction(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    //event是Method的参数
    public void execute(Object event) {
        try {
            method.setAccessible(true);
            method.invoke(target, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
