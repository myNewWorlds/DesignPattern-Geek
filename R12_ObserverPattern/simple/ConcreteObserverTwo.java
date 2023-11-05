package simple;

public class ConcreteObserverTwo implements Observer{
    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverTwo is notified.");
    }
}
