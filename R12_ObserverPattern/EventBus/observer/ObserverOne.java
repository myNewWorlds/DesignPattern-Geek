package EventBus.observer;

import EventBus.Subscribe;

public class ObserverOne {
    @Subscribe
    private void update(String userId) {
        System.out.println("输出UserID" + userId);
    }
}
