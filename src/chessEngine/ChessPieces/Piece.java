package chessEngine.ChessPieces;


import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;

import java.util.*;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;

    Piece(final PieceType pieceType,
          final int position,
          final Alliance pieceAlliance){
        this.pieceType = pieceType;
        this.alliance = pieceAlliance;
        this.position = position;
        isFirstMove = false;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getPosition() {
        return this.position;
    }

    public Alliance getAlliance(){
        return this.alliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public enum PieceType {

        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        } ,
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
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

    }
}
