package chessEngine.ChessPieces;

import chessEngine.Alliance;
import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Move;
import chessEngine.ChessBoard.Move.*;
import chessEngine.ChessBoard.Tile;
import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.List;

import static chessEngine.ChessBoard.BoardUtils.*;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight(int position, final Alliance alliance){
        super(position, alliance);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentOffset:CANDIDATE_MOVE_COORDINATES){

            final int target = this.position + currentOffset;
            if(isValidCoordinate(target)){
                if(columnExclusion(this.position, currentOffset))
                    continue;

                final Tile targetTile = board.getTile(target);
                if(!targetTile.isTileOccupied()){
                    legalMoves.add(new StandardMove(board, this, target));
                }else{
                    final Piece TargetPiece = targetTile.getPiece();
                    final Alliance otherAlliance = TargetPiece.getAlliance();
                    if(this.alliance != otherAlliance){
                        legalMoves.add(new AttackMove(board, this, target, TargetPiece));
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    private static boolean columnExclusion(final int currentPosition, final int candidateOffset){
        return firstColumnExclusion(currentPosition, candidateOffset) ||
                secondColumnExclusion(currentPosition, candidateOffset) ||
                seventhColumnExclusion(currentPosition, candidateOffset) ||
                eighthColumnExclusion(currentPosition, candidateOffset);
    }

    private static boolean firstColumnExclusion(final int currentPosition, final int candidateOffset){
        return FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean secondColumnExclusion(final int currentPosition, final int candidateOffset){
        return SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean seventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return SEVENTH_COLUMN[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));
    }

    private static boolean eighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return EIGTH_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10) ||
                (candidateOffset == -6) || (candidateOffset == -15));
    }





}
