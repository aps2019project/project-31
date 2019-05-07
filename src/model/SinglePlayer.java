package model;

import constants.GameMode;
import controller.Menu;

public class SinglePlayer extends BattleManager {

    private static Ai aiPlayer = null;
    private static Account AIAccount = null;

    public SinglePlayer(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        super(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);

    }

    public static void makeAIAccount(Deck deck){
        AIAccount = new Account("AI","beep",20000);
        AIAccount.setTheMainDeck(deck);
        aiPlayer = new Ai(AIAccount);
    }

    public static Player getAiPlayer() {
         return aiPlayer;
    }
}
