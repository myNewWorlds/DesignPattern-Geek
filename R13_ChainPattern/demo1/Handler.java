package demo1;

public abstract class Handler {
    //当前Handler处理成功后，下一个处理的Handler
    protected Handler successor = null;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public final void handle() {
        boolean handled = dohandle();
        if (successor != null && !handled) {
            successor.handle();
        }
    }

    protected abstract boolean dohandle();
}
