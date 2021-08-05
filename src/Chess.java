package chessEngine;

import chessEngine.ChessBoard.Board;
import chessEngine.ChessBoard.Tile;

public class Chess {

    public static void main(String[] args) {

        Tile t = new Tile.EmptyTile(10);

        Board board = Board.createStandardBoard();
        System.out.println(board);
    }

}
