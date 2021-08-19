package chessEngine.ChessBoard;

import chessEngine.ChessBoard.Board.Builder;
import chessEngine.ChessPieces.Pawn;
import chessEngine.ChessPieces.Piece;
import chessEngine.ChessPieces.Rook;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destination;
    protected boolean isFirstMove;

    public static Move NULL_MOVE = new NullMove();

    private Move(final Board board,
         final Piece movedPiece,
         final int destination) {

        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
        if (movedPiece != null) {
            this.isFirstMove = movedPiece.isFirstMove();
        } else {
            this.isFirstMove = false;
        }


    }

    private Move(final Board board, final int destination) {
        this.board = board;
        this.movedPiece = null;
        this.destination = destination;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPosition();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if(!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getPosition() == otherMove.getPosition() &&
               getDestination() == otherMove.getDestination() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getPosition() {
        if (this.movedPiece != null) {
            return this.movedPiece.getPosition();
        }
        return -1;
    }

    public int getDestination() {
        return this.destination;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {

        final Builder builder = new Builder();

        for(final Piece piece : this.board.getPlayer().getActivePieces()) {
            if(!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for(final Piece piece : this.board.getPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        // Move the piece
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    @Override
    public String toString() {
        return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPosition())
                + " -> " + BoardUtils.getPositionAtCoordinate(this.destination);
    }

    public static class StandardMove extends Move {

        public StandardMove(final Board board,
                            final Piece movedPiece,
                            final int destination) {
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof StandardMove && super.equals(other);
        }
     }

    public static class AttackMove extends Move {

        final Piece target;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destination,
                          final Piece target) {
            super(board, movedPiece, destination);
            this.target = target;
        }

        @Override
        public int hashCode() {
            return this.target.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if(!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherMove = (AttackMove) other;
                    return super.equals(otherMove) &&
                            getAttackedPiece().equals(otherMove.getAttackedPiece());

        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.target;
        }

    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destination) {
            super(board, movedPiece, destination);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destination,
                              final Piece target) {
            super(board, movedPiece, destination, target);
        }
    }

    public static final class EnPassantMove extends PawnAttackMove {

        public EnPassantMove(final Board board,
                             final Piece movedPiece,
                             final int destination,
                             final Piece target) {
            super(board, movedPiece, destination, target);
        }
    }


    public static class PawnJump extends Move {

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destination) {
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getPlayer().getOpponent().getAlliance());
            return builder.build();

        }

    }

    public static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int rookStart;
        protected final int rookDestination;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destination,
                          final Rook castleRook,
                          final int rookStart,
                          final int rookDestination) {

            super(board, movedPiece, destination);
            this.castleRook = castleRook;
            this.rookStart = rookStart;
            this.rookDestination = rookDestination;

        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));

            //TODO: look into the first move on normal pieces
            builder.setPiece(new Rook(this.rookDestination, this.castleRook.getAlliance()));
            builder.setMoveMaker(this.board.getPlayer().getOpponent().getAlliance());
            return builder.build();

        }

    }

    public static class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destination,
                                  final Rook castleRook,
                                  final int rookStart,
                                  final int rookDestination) {
            super(board, movedPiece, destination, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return"0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destination,
                                   final Rook castleRook,
                                   final int rookStart,
                                   final int rookDestination) {
            super(board, movedPiece, destination, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return"0-0=0";
        }
    }

    public static class NullMove extends Move {

        public NullMove() {
            super(null, 65);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cant execute null move you dumbass");
        }

        @Override
        public int getPosition() {
            return -1;
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not Instantiable fuck");
        }

        public static Move createMove(final Board board,
                                      final int position,
                                      final int destination) {

            for(final Move move : board.getAllLegalMoves()) {
                if(move.getPosition() == position &&
                        move.getDestination() == destination) {
                    return move;
                }
            }
            return NULL_MOVE;

        }

    }

}
