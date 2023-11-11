package demo1;

public class HandlerA extends Handler{
    @Override
    protected boolean dohandle() {
        System.out.println("A处理");
        return false;
    }
}
