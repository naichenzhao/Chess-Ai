package chessEngine.Player;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Tile;
import chessEngine.ChessPieces.Piece;
import chessEngine.ChessPieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> kingCastles(final Collection<Move> playerMoves,
                                           final Collection<Move> opponentMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // Black's king side castle
            if(!this.board.getTile(5).isOccupied() &&
               !this.board.getTile(6).isOccupied()) {

                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getIncomingAttacks(5, opponentMoves).isEmpty() &&
                            Player.getIncomingAttacks(6, opponentMoves).isEmpty() &&
                            rookTile.getPiece().getType().isRook()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                        this.playerKing,
                                        6,
                                        (Rook) rookTile.getPiece(),
                                        rookTile.getCoordinate(),
                                        5));
                    }

                }
            }
        }

        if(!this.board.getTile(1).isOccupied() &&
           !this.board.getTile(2).isOccupied() &&
           !this.board.getTile(3).isOccupied()) {
            final Tile rookTile = this.board.getTile(0);
            if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
            Player.getIncomingAttacks(2, opponentMoves).isEmpty() &&
            Player.getIncomingAttacks(3, opponentMoves).isEmpty() &&
            rookTile.getPiece().getType().isRook()) {
                kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                this.playerKing,
                                62,
                                (Rook) rookTile.getPiece(),
                                rookTile.getCoordinate(),
                                59));
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
