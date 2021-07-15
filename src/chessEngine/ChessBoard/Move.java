package chessEngine.ChessBoard;

import chessEngine.ChessPieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destination;

    private Move(final Board board,
         final Piece movedPiece,
         final int destination) {

        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;

    }

    public static final class StandardMove extends Move {

        public StandardMove(final Board board,
                            final Piece movedPiece,
                            final int destination) {
            super(board, movedPiece, destination);
        }
    }

    public static final class AttackMove extends Move {

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destination,
                          final Piece target) {
            super(board, movedPiece, destination);
        }
    }





}
