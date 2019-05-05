package model;

import constants.GameMode;
import controller.Menu;

public class MultiPlayer extends BattleManager{

    public MultiPlayer(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag) {
        super(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag);
    }
}
