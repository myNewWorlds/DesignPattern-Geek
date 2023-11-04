import java.awt.*;

/**
 * 享元模式的共享字体类
 */
public class CharacterStyle {
    private Font font;
    private int size;
    private int colorRGB;

    public CharacterStyle(Font font, int size, int colorRGB) {
        this.font = font;
        this.size = size;
        this.colorRGB = colorRGB;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterStyle) {
            CharacterStyle otherStyle = (CharacterStyle) obj;
            return font.equals(otherStyle.font) && size == otherStyle.size && colorRGB == otherStyle.colorRGB;
        }
        return false;
    }
}
