package chessEngine;

import chessEngine.Player.BlackPlayer;
import chessEngine.Player.Player;
import chessEngine.Player.WhitePlayer;

public enum Alliance {
    WHITE{
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final BlackPlayer blackPlayer,
                                   final WhitePlayer whitePlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer( final BlackPlayer blackPlayer,
                                    final WhitePlayer whitePlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();


    public abstract Player choosePlayer(BlackPlayer blackPlayer, WhitePlayer whitePlayer);
}
