import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyDemo {
    public static void main(String[] args) {
        UserController controller = new UserController();
        /**
         * newProxyInstance的3个参数
         * 1. 类加载器，定义用哪个类加载器加载代理对象
         * 2. 代理对象要实现的接口
         * 3. 代理方法执行invoke()内部的方法
         *
         * 4. 只能强制转换成接口，因为底层是基于接口的动态代理
         * 即，底层重新生成了一个实现所有接口的代理类，IUserController 有实现类UserController 与 $Proxy0
         * Proxy.newProxyInstance返回的是 $Proxy0，所以不能强制转换为UserController
         */
        IUserController controllerProxy = (IUserController) Proxy.newProxyInstance(UserController.class.getClassLoader(),
                UserController.class.getInterfaces(),
                new InvocationHandler() {
                    /**
                     * proxy 代理对象自己
                     * method 当前调用的原始方法
                     * args 参数
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("此处进行了代理" + method.getName());
                        return method.invoke(controller, args);
                    }
                });

        //代理类执行方法
        controllerProxy.login("", "");
    }
}
