package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.*;

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};

    public Pawn(int position, Alliance pieceAlliance) {
        super(PieceType.PAWN, position, pieceAlliance, true);
    }

    public Pawn(int position, Alliance pieceAlliance, boolean isFirstMove) {
        super(PieceType.KING, position, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int target = this.position + (this.getAlliance().getDirection() * candidateOffset);

            if (!isValidCoordinate(target)) {
                continue;
            }
            if (candidateOffset == 8 && !board.getTile(target).isOccupied()) {
                // Handles standard move
                //TODO: Add promotion
                legalMoves.add(new PawnJump(board, this, target));
            } else if (candidateOffset == 16 && this.isFirstMove() && checkStartingPosition()) {
                // Calculates jump move
                final int middleCoordinate = this.position + (this.getAlliance().getDirection() * 8);
                if (!board.getTile(middleCoordinate).isOccupied() && !board.getTile(target).isOccupied()) {
                    legalMoves.add(new StandardMove(board, this, target));
                }
            } else if (candidateOffset == 7 &&
                    !((EIGHTH_COLUMN[this.position] && alliance.isWhite()) ||
                    (FIRST_COLUMN[this.position] && alliance.isBlack()))) {
                // Handles attacking
                if(board.getTile(target).isOccupied()) {
                    final Piece targetPiece = board.getTile(target).getPiece();
                    if (targetPiece.getAlliance() != this.getAlliance()) {
                        // TODO: add more stuff
                        legalMoves.add(new PawnAttackMove(board, this, target, targetPiece));
                    }
                }
            } else if (candidateOffset == 9 &&
                    !((FIRST_COLUMN[this.position] && alliance.isWhite()) ||
                    (EIGHTH_COLUMN[this.position] && alliance.isBlack()))) {
                // Handles attacking
                if(board.getTile(target).isOccupied()) {
                    final Piece targetPiece = board.getTile(target).getPiece();
                    if (targetPiece.getAlliance() != this.getAlliance()) {
                        // TODO: add more stuff
                        legalMoves.add(new PawnAttackMove(board, this, target, targetPiece));
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestination(), move.getMovedPiece().getAlliance());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public boolean checkStartingPosition() {
        return (SECOND_ROW[this.position] && this.getAlliance().isBlack()) ||
                (SEVENTH_ROW[this.position] && this.getAlliance().isWhite());
    }

}
