package model;

import constants.GameMode;
import controller.Menu;

public class SinglePlayer extends BattleManager {

    private static Player aiPlayer;

    public SinglePlayer(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        super(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
    }

    static {
        Account AIAccount = new Account("AI", "beepboop", 100000);
        aiPlayer = new Ai(AIAccount);
    }

    public static Player getAiPlayer() {
        return aiPlayer;
    }
}
