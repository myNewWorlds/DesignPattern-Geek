package demo2;

public class HandlerA implements IHandler{
    @Override
    public boolean handle() {
        System.out.println("A处理");
        return false;
    }
}
