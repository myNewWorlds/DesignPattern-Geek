package myself;

/**
 * 单独的快照类，只能get数据，不能set数据
 */
public class Snapshot {
    private final String text;

    public Snapshot(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
