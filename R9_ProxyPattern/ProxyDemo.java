public class ProxyDemo {
    public static void main(String[] args) {
        UserControllerProxy userControllerProxy = new UserControllerProxy(new UserController());
        userControllerProxy.login("", "");
    }
}
