package model;

import constants.FunctionType;
import constants.GameMode;
import controller.BattleMenu;
import controller.BattlePageController;
import javafx.application.Platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameRecord {
    protected String game = " ";
    protected Player player1;
    protected Player player2;
    protected int maxNumberOfFlags;
    protected int maxTurnsOfHavingFlag;
    protected GameMode gameMode;
    protected BattleManager battleManager;

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

    public void makeFormalBattleManagerForRecord() { // first thing to show record is to call this!
        BattleMenu.deleteBattleManagerAndMakeMap();
        BattleManager battleManager = new BattleManager(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
        BattleMenu.setBattleManager(battleManager);
        this.battleManager = battleManager;
    }


    public void showTheWholeGame() { //WOW!!!!
        String[] actions = game.split("\\+");

        for (String action : actions) {
            if (action.startsWith("E"))
                battleManager.showThatGameEnded();
            if (action.startsWith("T")) {

            }
            if (action.contains("A"))
                checkIfAttack(action);
            if (action.contains("I"))
                checkIfInsert(action);
            if (action.contains("M"))
                checkIfMove(action);
            Platform.runLater(() -> {
                BattlePageController.getInstance().refreshTheStatusOfMap(battleManager);
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIfAttack(String action) {
        Pattern pattern = Pattern.compile("\\dA(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            int x1 = Integer.parseInt(matcher.group(1));
            int x2 = Integer.parseInt(matcher.group(2));
            int x_1 = Integer.parseInt(matcher.group(3));
            int x_2 = Integer.parseInt(matcher.group(4));
            Cell attackerCell = Map.getInstance().getCell(x1, x2);
            Cell enemyCell = Map.getInstance().getCell(x_1, x_2);
            battleManager.doTheActualAttack_noTarof(attackerCell.getCardInCell(),enemyCell.getCardInCell());
        }
    }

    private void checkIfMove(String action) {

    }

    private void checkIfInsert(String action) {

    }

    private void formalEndTurn() {
        doThingsAtEndOfTurn();
        battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
        doThingsAtBeginningOfTurn();

        Platform.runLater(() -> {
            BattlePageController.getInstance().refreshTheStatusOfMap(battleManager);
        });
    }

    public void doThingsAtEndOfTurn() {
        battleManager.makeIsMovedAndIsAttackedFalse();
        battleManager.applyItemFunctions(battleManager.getPlayer1().getHero(), FunctionType.Passive);
        battleManager.getCurrentPlayer().endOfTurnBuffsAndFunctions();
        battleManager.getOtherPlayer().endOfTurnBuffsAndFunctions();
        battleManager.addTurn();
        battleManager.getPlayer1().getHero().getHeroSpell().decrementCooldonwRemaining();
        battleManager.getPlayer2().getHero().getHeroSpell().decrementCooldonwRemaining();
    }

    private void doThingsAtBeginningOfTurn() {
        battleManager.assignManaToPlayers();
    }

}
