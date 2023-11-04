/**
 * 享元类，对象初始化后不会改变
 */
public class ChessPieceUnit {
    private int id;
    private String text;
    private Color color;
    public static enum Color{
        RED,BLACK
    }

    public ChessPieceUnit(int id, String text, Color color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }
}
