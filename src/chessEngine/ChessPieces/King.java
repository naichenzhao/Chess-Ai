package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Move.*;
import chessEngine.ChessBoard.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.*;

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int position, Alliance pieceAlliance) {
        super(PieceType.KING, position, pieceAlliance, true);
    }

    public King(int position, Alliance pieceAlliance, boolean isFirstMove) {
        super(PieceType.KING, position, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int coordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int target = this.position + coordinateOffset;

            if (columnExclusion(this.position, coordinateOffset))
                continue;

            if (isValidCoordinate(target)) {
                final Tile targetTile = board.getTile(target);

                if (!targetTile.isOccupied()) {
                    if (board.getBlackPlayer() != null && board.getWhitePlayer() != null) {
                        Collection<Move> blackMoves = board.getBlackPlayer().getLegalMoves();
                        Collection<Move> whiteMoves = board.getWhitePlayer().getLegalMoves();
                        Collection<Move> allMoves = this.alliance.isBlack() ? blackMoves : whiteMoves;
                        if (getIncomingAttacks(target, allMoves).isEmpty()) {
                            legalMoves.add(new StandardMove(board, this, target));
                        }
                    } else {
                        legalMoves.add(new StandardMove(board, this, target));
                    }

                } else {
                    final Piece targetPiece = targetTile.getPiece();
                    final Alliance otherAlliance = targetPiece.getAlliance();
                    if (this.alliance != otherAlliance) {
                        legalMoves.add(new AttackMove(board, this, target, targetPiece));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getDestination(), move.getMovedPiece().getAlliance());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    protected static Collection<Move> getIncomingAttacks(int targetPosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();

        for(final Move move : moves) {
            if(targetPosition == move.getDestination()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private static boolean columnExclusion(final int position, final int offset) {
        return firstColumnExclusion(position, offset) ||
                eighthColumnExclusion(position, offset);
    }

    private static boolean firstColumnExclusion(final int position, final int offset) {
        return FIRST_COLUMN[position] && (offset == -9 || offset == -1 || offset == 7);
    }

    private static boolean eighthColumnExclusion(final int position, final int offset) {
        return EIGHTH_COLUMN[position] && (offset == 9 || offset == 1 || offset == -7);
    }

}
