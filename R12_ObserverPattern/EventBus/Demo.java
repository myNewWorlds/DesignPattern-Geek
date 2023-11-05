package EventBus;

import EventBus.observer.ObserverOne;
import EventBus.observer.ObserverThree;
import EventBus.observer.ObserverTwo;

public class Demo {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new ObserverOne());
        eventBus.register(new ObserverTwo());
        eventBus.register(new ObserverThree());
        eventBus.post("2332");
        eventBus.post(23L);
        eventBus.post(new Object());
    }
}
