import java.util.HashMap;
import java.util.Map;

/**
 * 使用工厂类构建享元对象
 */
public class ChessPieceUnitFactory {
    //享元对象Map
    private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();

    static {
        pieces.put(1, new ChessPieceUnit(1, "车", ChessPieceUnit.Color.BLACK));
        pieces.put(2, new ChessPieceUnit(2, "车", ChessPieceUnit.Color.BLACK));
    }

    public static ChessPieceUnit getChessPiece(int chessPieceId) {
        return pieces.get(chessPieceId);
    }
}
