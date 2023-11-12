package table;

/**
 * 马里奥遇见的事件
 */
public enum Event {
    GOT_MUSHROOM(0),
    GOT_CAPE(1),
    GOT_FIRE(2),
    MET_MONSTER(3);
    private final int value;

    public int getValue() {
        return value;
    }

    Event(int value) {
        this.value = value;
    }
}
