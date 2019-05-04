package model;

import constants.GameMode;

public class SinglePlayer extends BattleManager {


    public SinglePlayer(Map map, GameMode gameMode, Player currentPlayer, int maxNumberOfFlags, Player playerOne, Player aiPlayer) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags);
    }

    public static void playAi(){

    }

    @Override
    public Player getOtherPlayer() {
        if(player1.isAi())
            return player1;
        else if (player2.isAi())
            return player2;
        return null;
    }
}
