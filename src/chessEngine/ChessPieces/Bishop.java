package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Move.AttackMove;
import chessEngine.ChessBoard.Move.StandardMove;
import chessEngine.ChessBoard.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.*;

public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    public Bishop(int position, Alliance alliance) {
        super(PieceType.BISHOP, position, alliance);
    }




    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int coordinateOffset: CANDIDATE_MOVE_COORDINATES) {
            int target = this.position;

            if(columnExclusion(position, coordinateOffset))
                break;

            while(isValidCoordinate(target)) {

                target += coordinateOffset;
                if (isValidCoordinate(target)) {
                    final Tile targetTile = board.getTile(target);
                    if (!targetTile.isTileOccupied()) {
                        legalMoves.add(new StandardMove(board, this, target));
                    } else {
                        final Piece targetPiece = targetTile.getPiece();
                        final Alliance otherAlliance = targetPiece.getAlliance();
                        if (this.alliance != otherAlliance) {
                            legalMoves.add(new AttackMove(board, this, target, targetPiece));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean columnExclusion(final int position, final int offset) {
        return firstColumnExclusion(position, offset) ||
                eighthColumnExclusion(position, offset);
    }

    private static boolean firstColumnExclusion(final int position, final int offset) {
        return FIRST_COLUMN[position] && (offset == -9 || offset == 7);
    }

    private static boolean eighthColumnExclusion(final int position, final int offset) {
        return FIRST_COLUMN[position] && (offset == 9 || offset == -7);
    }

}
