/**
 * 业务类
 * 职责单一，只聚焦业务处理
 */
public class UserController implements IUserController{
    @Override
    public UserVo login(String telephone, String password) {
        //具体登录业务
        System.out.println("具体登录业务");
        return null;
    }

    @Override
    public UserVo register(String telephone, String password) {
        //具体注册业务
        System.out.println("具体注册业务");
        return null;
    }
}
