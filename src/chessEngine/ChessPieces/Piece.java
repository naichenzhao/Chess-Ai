package chessEngine.ChessPieces;


import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;

import java.util.*;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    Piece(final int piecePosition, final Alliance pieceAlliance){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

}
