package chessEngine.ChessBoard;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = null;
    public static final boolean[] SECOND_COLUMN = null;
    public static final boolean[] SEVENTH_COLUMN = null;
    public static final boolean[] EIGTH_COLUMN = null;

    private BoardUtils(){
        throw new RuntimeException("You shant initate me fool!");
    }

    public static boolean isValidTileCoordinate(int coordinate){
        return coordinate >= 0 && coordinate <= 64;
    }


}
