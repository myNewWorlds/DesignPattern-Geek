package table;

/**
 * 马里奥的各种状态
 */
public enum State {
    SMALL(0),
    SUPER(1),
    FIRE(2),
    CAPE(3);
    private final int value;

    public int getValue() {
        return value;
    }

    State(int value) {
        this.value = value;
    }
}
