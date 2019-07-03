package model;

import constants.GameMode;
import controller.BattleMenu;

public class GameRecord {
    protected String game = " ";
    protected Player player1;
    protected Player player2;
    protected int maxNumberOfFlags;
    protected int maxTurnsOfHavingFlag;
    protected GameMode gameMode;

    public GameRecord(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        this.player1 = player1;
        this.player2 = player2;
        this.maxNumberOfFlags = maxNumberOfFlags;
        this.maxTurnsOfHavingFlag = maxTurnsOfHavingFlag;
        this.gameMode = gameMode;
    }

    public void addAction(String action) {
        game += "+" + action;
    }

    public void makeFormalBattleManagerForRecord() {
        BattleManager battleManager = new BattleManager(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
        BattleMenu.setBattleManager(battleManager);
    }
}
