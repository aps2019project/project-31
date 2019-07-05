package model;

import constants.FunctionType;
import constants.GameMode;
import controller.BattleMenu;
import controller.MultiPlayerBattlePageController;
import controller.SinglePlayerBattlePageController;
import controller.Shop;
import javafx.application.Platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameRecord {
    protected String game = "";
    protected Player player1;
    protected Player player2;
    protected int maxNumberOfFlags;
    protected int maxTurnsOfHavingFlag;
    protected GameMode gameMode;
    protected BattleManager battleManager;
    protected Cell[][] map;

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

    public Cell[][] getMap() {
        return map;
    }

    public void setMap(Cell[][] map) {
        this.map = map;
    }

    public void makeFormalBattleManagerForRecord() { // first thing to show record is to call this!
        BattleMenu.deleteBattleManagerAndMakeMap();
        BattleManager battleManager = new BattleManager(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode, false);
        BattleMenu.setBattleManager(battleManager);
        this.battleManager = battleManager;

    }


    public void showTheWholeGame() { //WOW!!!!
        Map.getInstance().setMap(map);
        String[] actions = game.split("\\+");

        Map.getInstance().getCell(3, 1).setCardInCell(battleManager.getPlayer1().getHero());
        Map.getInstance().getCell(3, 9).setCardInCell(battleManager.getPlayer2().getHero());
        GameCompiler gameCompiler = new GameCompiler(battleManager);
        for (String action : actions) {
            if (action.startsWith("T")) {
                formalEndTurn();
            }
            gameCompiler.whatIsThePlay(action, gameCompiler);
            if (battleManager.isMultiPlayer())
                MultiPlayerBattlePageController.getInstance().refreshTheStatusOfMap(battleManager);
            else
                SinglePlayerBattlePageController.getInstance().refreshTheStatusOfMap(battleManager);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void formalEndTurn() {
        doThingsAtEndOfTurn();
        battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
        doThingsAtBeginningOfTurn();

        Platform.runLater(() -> {
            SinglePlayerBattlePageController.getInstance().refreshTheStatusOfMap(battleManager);
        });
    }

    public void doThingsAtEndOfTurn() {
        battleManager.makeIsMovedAndIsAttackedFalse();
        battleManager.applyItemFunctions(battleManager.getPlayer1().getHero(), FunctionType.Passive);
        battleManager.getCurrentPlayer().endOfTurnBuffsAndFunctions();
        battleManager.getOtherPlayer().endOfTurnBuffsAndFunctions();
        BattleMenu.getBattleManager().flagModeSitAndAddTurnAndHeroSpellSit();
    }

    private void doThingsAtBeginningOfTurn() {
        battleManager.assignManaToPlayers();
        //     BattleMenu.isTimeToPutItem(); different kind of item put on map (than the one that actually happened)
    }

    public String getGame() {
        return game;
    }
}
