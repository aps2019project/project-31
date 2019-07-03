package model;

import constants.GameMode;

public class GameRecord {
    protected String game = "+";
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
}
