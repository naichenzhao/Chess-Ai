package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.*;

public class Queen extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(int position, Alliance pieceAlliance) {
        super(PieceType.QUEEN, position, pieceAlliance, true);
    }

    public Queen(int position, Alliance pieceAlliance, boolean isFirstMove) {
        super(PieceType.KING, position, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int coordinateOffset: CANDIDATE_MOVE_COORDINATES) {
            int target = this.position;

            while(isValidCoordinate(target)) {
                if(columnExclusion(target, coordinateOffset))
                    break;

                target += coordinateOffset;
                if(isValidCoordinate(target)) {
                    final Tile targetTile = board.getTile(target);
                    if(!targetTile.isOccupied()){
                        legalMoves.add(new Move.StandardMove(board, this, target));
                    }else{
                        final Piece TargetPiece = targetTile.getPiece();
                        final Alliance otherAlliance = TargetPiece.getAlliance();
                        if(this.alliance != otherAlliance){
                            legalMoves.add(new Move.AttackMove(board, this, target, TargetPiece));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestination(), move.getMovedPiece().getAlliance());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    private static boolean columnExclusion(final int position, final int offset) {
        return firstColumnExclusion(position, offset) ||
                eighthColumnExclusion(position, offset);
    }

    private static boolean firstColumnExclusion(final int position, final int offset) {
        return position < NUM_TILES && FIRST_COLUMN[position] && (offset == -9 || offset == 7 || offset == -1);
    }

    private static boolean eighthColumnExclusion(final int position, final int offset) {
        return position < NUM_TILES && EIGHTH_COLUMN[position] && (offset == 9 || offset == -7 || offset == 1);
    }

}
