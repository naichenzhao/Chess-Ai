package chessEngine.Player;

import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;

public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board board,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getBoard() {
        return this.transitionBoard;
    }

}
