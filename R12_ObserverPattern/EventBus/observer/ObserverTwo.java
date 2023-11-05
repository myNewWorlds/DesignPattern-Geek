package EventBus.observer;

import EventBus.Subscribe;

public class ObserverTwo {
    @Subscribe
    private void update(Long length) {
        System.out.println("长度" + length);
    }
}
