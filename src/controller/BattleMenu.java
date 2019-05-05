package controller;

import constants.*;
import constants.GameMode;
import model.*;
import view.Input;
import view.Output;

import java.util.ArrayList;

public class BattleMenu extends Menu {
    private static BattleManager battleManager;
    private static boolean areWeInMiddleOfTurn = true;
    protected static boolean isGameFinished = false;

    public static boolean isGameFinished() {
        return isGameFinished;
    }

    public static void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public static void showPlayerMinions(Player player) {
        for (Deployable deployable : player.getCardsOnBattleField()) {
            Output.print(deployable.infoToString());
        }
    }

    public static boolean isAreWeInMiddleOfTurn() {
        return areWeInMiddleOfTurn;
    }

    public static void setAreWeInMiddleOfTurn(boolean areWeInMiddleOfTurn) {
        BattleMenu.areWeInMiddleOfTurn = areWeInMiddleOfTurn;
    }

    public static BattleManager getBattleManager() {
        return battleManager;
    }

    public static void setBattleManagerMode() {
        //       battleManager = new ();


    }

    public static void setBattleManagerMode(BattleManager battleManager) {
        // battleManager = new
    }

    public void doAllAtTheBeginingOfTurnThings(){
        battleManager.assignManaToPlayers();
        battleManager.manaAdderItem();

    }
    public void runTheGame() {
        boolean isPlayer1Turn = false;
        battleManager.setCurrentPlayer(battleManager.getPlayer2());
        battleManager.applyItemFunctions(battleManager.getCurrentPlayer().getHero(), FunctionType.GameStart);
        BattleManager.initialTheGame();
        while (true) {
            isPlayer1Turn = !isPlayer1Turn;
            battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
            doAllAtTheBeginingOfTurnThings();
            if (battleManager.getCurrentPlayer().isAi()) {
                ((Ai) battleManager.getCurrentPlayer()).play();
            } else {
                areWeInMiddleOfTurn = true;
                boolean sit = true;
                while (sit == areWeInMiddleOfTurn) {
                    Input.handleCommandsInBattleMenu(battleManager.getCurrentPlayer(),
                            battleManager.getCurrentPlayer().getSelectedCard() != null);
                    if (isGameFinished) {
                        battleManager = null;
                        run();
                    }

                }
            }
            doAllThingsInEndingOfTheTurns();
            Output.theTurnEnded();
        }
    }

    private static void doAllThingsInEndingOfTheTurns() {
        battleManager.applyItemFunctions(battleManager.getPlayer1().getHero(), FunctionType.Passive);
        battleManager.getCurrentPlayer().placeNextCardToHand();
        battleManager.getCurrentPlayer().endOfTurnBuffsAndFunctions();
        battleManager.getOtherPlayer().endOfTurnBuffsAndFunctions();
        battleManager.checkTheEndSituation();
        if (BattleManager.getGameMode() == GameMode.Flag) {
            battleManager.getPlayer1().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
            battleManager.getPlayer2().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
        }
        battleManager.addTurn();

    }


    public void run() {
        if (!Account.getMainAccount().getTheMainDeck().checkIfValid()) {
            System.err.println("selected deck is invalid");
            return;
        }
        while (true) {

            Output.showGameModes();

            //if (out)
            //    break;
        }
    }


    public void show() {

    }


    public void help() {

    }


    public static void prepareComboAttack(String[] strNumbers, int opponentCardId) {
        ArrayList<Deployable> validCards = new ArrayList<>();
        for (String number : strNumbers) {
            int cardId = Integer.parseInt(number);
            if (Map.findCellByCardId(cardId).getCardInCell() != null &&
                    Map.findCellByCardId(opponentCardId).getCardInCell() != null &&
                    BattleManager.isAttackTypeValidForAttack(Map.findCellByCardId(cardId).getCardInCell(),
                            Map.findCellByCardId(opponentCardId).getCardInCell())) {
                if (Map.findCellByCardId(cardId).getCardInCell().isCombo())
                    validCards.add(Map.findCellByCardId(cardId).getCardInCell());
            }
        }
        BattleMenu.getBattleManager().comboAtack(Map.findCellByCardId(opponentCardId).getCardInCell(), validCards);
    }

    public static void attack(int uniqueCardId) {
        Card selectedCard = BattleMenu.getBattleManager().getCurrentPlayer().getSelectedCard();
        if (selectedCard != null && battleManager.getCurrentPlayer().isSelectedCardDeployed()) {
            if (Map.findCellByCardId(uniqueCardId) != null)
                battleManager.attack((Deployable) selectedCard
                        , Map.findCellByCardId(uniqueCardId).getCardInCell());
            else
                Output.enemyNotExist();
        } else {
            Output.badSelectedCard();
        }
    }

    public static void move(int x1, int x2) {
        if (battleManager.getCurrentPlayer().isSelectedCardDeployed())
            battleManager.move((Deployable) (battleManager.getCurrentPlayer().getSelectedCard()), x1, x2);
    }

    public static boolean insert(Card card, int x1, int x2) {
        if (battleManager.cardInHandByCardId(card.getId()) != null) {
            if (!battleManager.checkCoordinates(x1, x2)) {
                Output.invalidInsertionTarget();
                System.err.println("Invalid Coordinates");
                return false;

            }

            if (card.getManaCost() > battleManager.getCurrentPlayer().getMana()) {
                Output.notHaveEnoughMana();
                System.err.println("Not enough mana");
                return false;
            }

            if (card.getType() == CardType.minion) {

                battleManager.playMinion((Minion) card, x1, x2);
            }
            if (card.getType() == CardType.spell) {

                battleManager.playSpell((Spell) card, x1, x2);
            }
            if (card.getType() == CardType.item) {
                battleManager.useItem((Item) card, x1, x2);
            }
            for (Function function : card.getFunctions()) {
                if (function.getFunctionType() == FunctionType.OnSpawn) {
                    battleManager.compileFunction(function, x1, x2);
                }
            }
        } else {
            Output.notInHand();
            System.err.println("Minion not in hand");
            return false;
        }
        return true;
    }

    public static void selectCollectibleItem(int cardUniqueId) {
        for (Deployable deployable : battleManager.getCurrentPlayer().getCardsOnBattleField()) {
            if (deployable.getItem().getUniqueId() == cardUniqueId)
                battleManager.getCurrentPlayer().setSelectedCard(deployable.getItem());
        }
    }

    public static void showGameInfo() {
        if (BattleManager.getGameMode() == GameMode.DeathMatch) {
            Output.print("Game Mode: Death Match\nPlayer1 Hero health: " + battleManager.getPlayer1().getHero().
                    getCurrentHealth() + "\nPlayer2 Hero health: " + battleManager.getPlayer2().getHero().
                    getCurrentHealth());
        } else if (BattleManager.getGameMode() == GameMode.Flag && BattleManager.flagPosition()[0] != -1) {
            Output.print("Game Mode: Flag\nFlag Position: " + BattleManager.flagPosition()[0] + " , " +
                    BattleManager.flagPosition()[1]);
        } else {
            Output.print("Team 1, cards with flags are:\n");
            for (Deployable deployable : battleManager.getPlayer1().getCardsOnBattleField()) {
                if (deployable.doesHaveFlag())
                    Output.print(deployable.infoToString());
            }
            Output.print("Team 2, cards with flags are:\n");
            for (Deployable deployable : battleManager.getPlayer2().getCardsOnBattleField()) {
                if (deployable.doesHaveFlag())
                    Output.print(deployable.infoToString());
            }
        }

    }

    public static void showSelectedCardInfo() {
        System.out.println(battleManager.getCurrentPlayer().getSelectedCard().infoToString());
    }

    public static Deployable findDeadMinion(int uniqueId) {
        for (Deployable deployable : battleManager.getPlayer1().getGraveYard()) {
            if (deployable.getUniqueId() == uniqueId)
                return deployable;
        }
        for (Deployable deployable : battleManager.getPlayer2().getGraveYard()) {
            if (deployable.getUniqueId() == uniqueId)
                return deployable;
        }
        return null;

    }
}

