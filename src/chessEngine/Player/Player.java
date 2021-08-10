package chessEngine.Player;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessPieces.King;
import chessEngine.ChessPieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    protected final boolean isInCheck;

    public Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, kingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.getIncomingAttacks(this.playerKing.getPosition(), opponentMoves).isEmpty();

    }

    public King getKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
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

    private King establishKing() {
        for(final Piece piece: getActivePieces()) {
            if(piece.getType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("You fucked something up buddy");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves() {

        for(final Move move : this.legalMoves) {
            final MoveTransition trans = makeMove(move);
            if(trans.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transBoard = move.execute();

        final Collection<Move> kingAttacks = Player.getIncomingAttacks(
                transBoard.getPlayer().getOpponent().getKing().getPosition(),
                transBoard.getPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> kingCastles( final Collection<Move> playerMoves,
                                                     final Collection<Move> opponentMoves);

}
