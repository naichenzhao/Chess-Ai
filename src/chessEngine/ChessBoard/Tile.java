package chessEngine.ChessBoard;

import chessEngine.ChessPieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected final int coordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i<BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }


    private Tile(final int coordinate){
        this.coordinate = coordinate;

    }

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece!= null? new OccupiedTile(tileCoordinate, piece ): EMPTY_TILES_CACHE.get(tileCoordinate);
    }


    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    public int getCoordinate() {
        return this.coordinate;
    }

    public static final class EmptyTile extends Tile{
        public EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isOccupied(){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }

    }

    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;

        OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getAlliance().isBlack() ?
                    getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean isOccupied(){
            return true;
        }

        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    }


}
