package chessEngine.ChessPieces;


import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;

import java.util.*;

public abstract class Piece {

    protected final PieceType type;
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
          final int position,
          final Alliance pieceAlliance,
          final boolean isFirstMove){
        this.type = pieceType;
        this.alliance = pieceAlliance;
        this.position = position;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = this.calculateHashCode();
    }

    private int calculateHashCode() {
        final int prime = 31;
        int result = this.type.hashCode();
        result = prime * result + this.alliance.hashCode();
        result = prime * result + this.position;
        result = prime * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if (!(other instanceof  Piece)) {
            return false;
        }

        final Piece otherPiece = (Piece) other;
        return this.position == otherPiece.position &&
                this.type == otherPiece.getType() &&
                this.alliance == otherPiece.getAlliance() &&
                this.isFirstMove == otherPiece.isFirstMove();

    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public PieceType getType() {
        return type;
    }

    public int getPosition() {
        return this.position;
    }

    public Alliance getAlliance(){
        return this.alliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        } ,
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
