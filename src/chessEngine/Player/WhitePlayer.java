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

import static chessEngine.ChessBoard.Move.*;

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> kingCastles(final Collection<Move> playerMoves,
                                           final Collection<Move> opponentMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // Whites king side castle move
            if(!this.board.getTile(61).isOccupied() &&
               !this.board.getTile(62).isOccupied()) {

                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.getIncomingAttacks(61, opponentMoves).isEmpty() &&
                       Player.getIncomingAttacks(62, opponentMoves).isEmpty() &&
                       rookTile.getPiece().getType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board,
                                        this.playerKing, 62,
                                        (Rook) rookTile.getPiece(),
                                        rookTile.getCoordinate(),61));
                    }

                }
            }
        }

        if(!this.board.getTile(59).isOccupied() &&
           !this.board.getTile(58).isOccupied() &&
           !this.board.getTile(57).isOccupied()) {
            final Tile rookTile = this.board.getTile(56);
            if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.getIncomingAttacks(59, opponentMoves).isEmpty() &&
                    Player.getIncomingAttacks(58, opponentMoves).isEmpty() &&
                    rookTile.getPiece().getType().isRook()) {
                kingCastles.add(new QueenSideCastleMove(this.board,
                                this.playerKing,
                                58,
                                (Rook) rookTile.getPiece(),
                                rookTile.getCoordinate(),
                                59));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
