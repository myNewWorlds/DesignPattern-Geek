package demo1;

public class HandlerB extends Handler{
    @Override
    protected boolean dohandle() {
        System.out.println("B处理");
        return false;
    }
}
