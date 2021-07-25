package chessEngine.ChessBoard;

import chessEngine.ChessPieces.Piece;
import chessEngine.ChessBoard.Board.*;

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

    public int getDestination() {
        return this.destination;
    }

    public abstract Board execute();

    public static final class StandardMove extends Move {

        public StandardMove(final Board board,
                            final Piece movedPiece,
                            final int destination) {
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();

            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setMoveMaker(null);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class AttackMove extends Move {

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destination,
                          final Piece target) {
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {
            return null;
        }
    }





}
