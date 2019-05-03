package controller;

import constants.GameMode;
import model.*;
import org.graalvm.compiler.replacements.Log;
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

    public static boolean isAreWeInMiddleOfTurn() {
        return areWeInMiddleOfTurn;
    }

    public static void setAreWeInMiddleOfTurn(boolean areWeInMiddleOfTurn) {
        BattleMenu.areWeInMiddleOfTurn = areWeInMiddleOfTurn;
    }

    public static BattleManager getBattleManager() {
        return battleManager;
    }

    private void setBattleManagerMode() {
        //       battleManager = new ();


        runTheGame(battleManager);
    }

    private void runTheGame(BattleManager battleManager) {
        boolean isPlayer1Turn = false;
        battleManager.setCurrentPlayer(battleManager.getPlayer2());
        BattleManager.initialTheGame();
        while (true) {
            isPlayer1Turn = !isPlayer1Turn;
            battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
            if (battleManager.getCurrentPlayer().isAi()) {
                ((Ai) battleManager.getCurrentPlayer()).play();
            } else {
                areWeInMiddleOfTurn = true;
                boolean sit = true;
                while (sit == areWeInMiddleOfTurn) {
                    if (battleManager.getCurrentPlayer().getSelectedCard() != null) {
                        Input.handleSelectCardOrSelectComboCards(battleManager.getCurrentPlayer());
                    } else {
                        Input.moveAttackPlayCard();
                    }
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

    private void doAllThingsInEndingOfTheTurns() {
        battleManager.getCurrentPlayer().placeNextCardToHand();
        battleManager.getCurrentPlayer().endOfTurnBuffsAndFunctions();
        battleManager.getOtherPlayer().endOfTurnBuffsAndFunctions();
        battleManager.checkTheEndSituation();
        if (BattleManager.getGameMode() == GameMode.Flag) {
            battleManager.getPlayer1().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
            battleManager.getPlayer2().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
        }
    }


    public void run() {
        if (!account.getTheMainDeck().checkIfValid())
            return;
        while (true) {
            /*View.showModes();
            handleInputCommand();
            if (out)
                break;*/
        }
    }


    public void show() {

    }


    public void help() {

    }

    private void handleInputCommand() {


        setBattleManagerMode();
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

    public static boolean insert(int cardId, int x1, int x2) {
        boolean canInsert = true;
        if (battleManager.cardInHandByCardId(cardId) != null) {
            Card card = battleManager.cardInHandByCardId(cardId);
            if (card.getType() == Card.CardType.minion) {
                canInsert = battleManager.playMinion((Minion)card, x1, x2);
            }
            if (card.getType() == Card.CardType.spell) {

                canInsert = battleManager.playSpell((Spell)card, x1, x2);
            }
            if (card.getType() == Card.CardType.item) {
                canInsert = battleManager.useItem((Item)card,x1,x2);
            }
        } else {
            Output.notInHand();
            Log.println("Minion not in hand");
            return false;
        }
        return canInsert;
    }

}

