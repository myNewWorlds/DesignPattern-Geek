package demo2;

import java.util.ArrayList;
import java.util.List;

public class HandlerChain {
    //基于数组实现
    private final List<IHandler> handlers = new ArrayList<>();

    public void addHandler(IHandler handler) {
        handlers.add(handler);
    }

    public void handle() {
        for (IHandler handler : handlers) {
            boolean handled = handler.handle();
            if (handled) {
                break;
            }
        }
    }
}
