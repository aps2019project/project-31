package controller;

import conatants.GameMode;
import model.*;
import org.graalvm.compiler.replacements.Log;
import view.Input;

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
            // say the turn ended
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

    @Override
    public void run() {
        if (!account.checkIfTheDeckIsValid())
            return;
        while (true) {
            /*View.showModes();
            handleInputCommand();
            if (out)
                break;*/
        }
    }

    @Override
    public void show() {

    }

    @Override
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

    public static void attack(int cardId) {
        Card selectedCard = BattleMenu.getBattleManager().getCurrentPlayer().getSelectedCard();
        if (selectedCard != null && battleManager.getCurrentPlayer().isSelectedCardDeployed()) {
            battleManager.attack((Deployable) (battleManager.getCurrentPlayer().getSelectedCard())
                    , Map.findCellByCardId(cardId).getCardInCell());
        } else {
            //invalid card id message
        }
    }

    public static void move(int x1, int x2) {
        if (battleManager.getCurrentPlayer().isSelectedCardDeployed())
            battleManager.move((Deployable) (battleManager.getCurrentPlayer().getSelectedCard()), x1, x2);
    }

    public static void insert(int cardId, int x1, int x2) {
        if (battleManager.cardInHandByCardId(cardId) != null) {
            /*Card card = battleManager.cardInHandByCardId(cardId);
            if (card.getType() == Card.CardType.minion) {
                Minion minion = new Minion();
                battleManager.playMinion(minion, x1, x2);
            }
            if (card.getType() == Card.CardType.spell) {
                Spell spell = new Spell();
                battleManager.playSpell(spell, x1, x2);
            }
            if(card.getType()== Card.CardType.item){
                Item item = new Item();
                battleManager.playItem();
            }*/
        } else {
            //insert not in hand error message for view
            Log.println("Minion not in hand");
            return;
        }
    }
}

