package demo2;

public class HandlerB implements IHandler{
    @Override
    public boolean handle() {
        System.out.println("B处理");
        return false;
    }
}
