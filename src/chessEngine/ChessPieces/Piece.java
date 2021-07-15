package chessEngine.ChessPieces;


import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;

import java.util.*;

public abstract class Piece {

    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirtMove;

    Piece(final int position, final Alliance pieceAlliance){
        this.alliance = pieceAlliance;
        this.position = position;
        isFirtMove = false;
    }

    public Alliance getAlliance(){
        return this.alliance;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFirstMove() {
        return isFirtMove;
    }
}
