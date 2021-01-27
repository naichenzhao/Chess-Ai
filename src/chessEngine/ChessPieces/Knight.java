package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.BoardUtils;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.isValidTileCoordinate;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight(int piecePosition, final Alliance pieceAlliance){
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(isValidTileCoordinate(candidateDestinationCoordinate)){

                if(isColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move());
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move());
                    }

                }

            }

        }
        return ImmutableList.copyOf(legalMoves);
    }


    private static boolean isColumnExclusion(final int currentPosition, final int candidateOffset){
        return isFirstColumnExclusion(currentPosition, candidateOffset) ||
                isSecondColumnExclusion(currentPosition, candidateOffset) ||
                isSeventhColumnExclusion(currentPosition, candidateOffset) ||
                isEigthColumnExclusion(currentPosition, candidateOffset);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));
    }

    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGTH_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10) ||
                (candidateOffset == -6) || (candidateOffset == -15));
    }





}
