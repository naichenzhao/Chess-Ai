package chessEngine.ChessBoard;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGTH_COLUMN = initColumn(8);

    public static final Boolean[] SECOND_ROW = null;
    public static final Boolean[] SEVENTH_ROW = null;

    public static int NUM_TILES = 64;
    public static final int TILES_PER_ROW = 8;

    private BoardUtils(){
        throw new RuntimeException("You shant initate me fool!");
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return column;
    }

    public static boolean isValidCoordinate(final int coordinate){
        return coordinate >= 0 && coordinate <= NUM_TILES;
    }


}
