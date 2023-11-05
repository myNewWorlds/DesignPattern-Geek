package EventBus.observer;

import EventBus.Subscribe;

public class ObserverThree {
    @Subscribe
    private void update(Object length) {
        System.out.println("Object对象" + length);
    }
}
