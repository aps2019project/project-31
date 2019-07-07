package model;

import Server.ServerStrings;
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
        for (String action : actions) {
            doWhatIAmToldTo(action,battleManager);
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

    public void doWhatIAmToldTo(String action, BattleManager battleManager) {
        this.battleManager = battleManager;
        if (action.startsWith("T")) {
            formalEndTurn();
        }
        if(action.equals(ServerStrings.ENDTURN)){
            battleManager.endTurn();
        }
        if(action.contains(ServerStrings.GAMEENDED)){
            System.out.println("the game ended in client side, i dont know what to do !");
            if(action.charAt(0)=='1'){
                battleManager.player1Won();
            }else {
                battleManager.player2Won();
            }
        }
        if (action.startsWith("E")) {
            System.out.println("the game ended");
            SinglePlayerBattlePageController.getInstance().showThatGameEnded();
            System.out.println("the game ended ? wtf ???");
        }
        if (action.contains("A"))
            checkIfAttack(action);
        if (action.contains("M"))
            checkIfMove(action);
        if (action.contains("I"))
            checkIfInsert(action);

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


    public Deployable deployableInCell(String sX1, String sX2) {
        int x1 = Integer.parseInt(sX1);
        int x2 = Integer.parseInt(sX2);
        Cell deployableCell = Map.getInstance().getCell(x1, x2);
        return deployableCell.getCardInCell();
    }

    public void checkIfAttack(String action) {
        System.out.println("check if action is attack");
        Pattern pattern = Pattern.compile("\\dA(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was attack");
            battleManager.doTheActualAttack_noTarof(deployableInCell(matcher.group(1), matcher.group(2)),
                    deployableInCell(matcher.group(3), matcher.group(4)));
        }
    }

    public void checkIfMove(String action) {
        System.out.println("check if action is move");
        Pattern pattern = Pattern.compile("\\dM(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was move");
            battleManager.doTheActualMove_noTarof(deployableInCell(matcher.group(1), matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        }
    }

    public void checkIfInsert(String action) {
        System.out.println("check if action is insert");
        Pattern pattern = Pattern.compile("\\dI(\\d\\d\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was insert");
            int id = Integer.parseInt(matcher.group(1));
            int x1 = Integer.parseInt(matcher.group(2));
            int x2 = Integer.parseInt(matcher.group(3));
            Card card = Shop.findCardById(id);
            switch (card.getType()) {
                case minion:
                    battleManager.playMinion((Minion) card, x1, x2);
                    break;
                case spell:
                    battleManager.playSpell((Spell) card, x1, x2);
                    break;
                case herospell:
                    battleManager.playSpell((Spell) card, x1, x2);
                    break;
                case item:
                    battleManager.useItem((Item) card, x1, x2);
                    break;
            }
        }
    }

    public String getGame() {
        return game;
    }
}
