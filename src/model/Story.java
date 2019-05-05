package model;

import constants.GameMode;
import controller.BattleMenu;

import java.util.ArrayList;

public class Story extends SinglePlayer {
    private static BattleManager firstBattleManager;
    private static BattleManager secondBattleManager;
    private static BattleManager thirdBattleManager;
    public Story(int id, String title, ArrayList<Deck> decks) {
        super(id, title);
        this.decks = decks;
    }

    public static BattleManager getSecondBattleManager() {
        return secondBattleManager;
    }

    public static BattleManager getThirdBattleManager() {
        return thirdBattleManager;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public static BattleManager getFirstBattleManager() {
        return firstBattleManager;
    }

    private ArrayList<Deck> decks;

}
