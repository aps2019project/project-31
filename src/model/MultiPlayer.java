package model;

import constants.GameMode;

public class MultiPlayer extends BattleManager {

    public MultiPlayer(Map map, GameMode gameMode, Player currentPlayer, int maxNumberOfFlags) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags);
    }


    @Override
    public Player getOtherPlayer() {
        if (currentPlayer == player1)
            return player2;
        else
            return player1;
    }

}
