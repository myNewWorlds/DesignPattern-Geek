/**
 * 代理类
 * 1. 通过委托的方式调用原始类执行业务代码
 * 2. 在业务代码前后 附加其他逻辑代码
 */
public class UserControllerProxy implements IUserController{
    private final UserController userController;
    public UserControllerProxy(UserController userController) {
        this.userController = userController;
    }

    @Override
    public UserVo login(String telephone, String password) {
        System.out.println("登录前执行其他代码");
        userController.login(telephone, password);
        System.out.println("登录后执行其他代码");
        return null;
    }

    @Override
    public UserVo register(String telephone, String password) {
        System.out.println("注册前执行其他代码");
        userController.register(telephone, password);
        System.out.println("注册后执行其他代码");
        return null;
    }
}
