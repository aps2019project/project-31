package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;

public class Player {
    protected Account account;
    protected Deck currentDeck = new Deck("deck in game", account.getTheMainDeck().getHero().duplicateHero());
    protected int mana;
    protected int remainingTime;
    protected int numbereOfFlags;
    protected int numberOfTurnsHavingFlag;
    protected ArrayList<Card> hand;
    protected Card nextCard;
    protected ArrayList<Deployable> cardsOnBattleField;
    protected ArrayList<Deployable> graveYard;
    protected Card selectedCard;
    protected Card cardInReplace;
    protected BattleManager battle;
    private boolean isAi;

    public boolean isAi() {
        return isAi;
    }

    public Player(Account account) {
        this.account = account;
    }

    public Hero getHero() {
        return currentDeck.getHero();
    }

    public void addCardToBattlefield(Deployable card) {
        cardsOnBattleField.add(card);
    }

    public void removeFromHand(Card card) throws ConcurrentModificationException {
        hand.remove(card);
    }

    public void removeFromGraveYard(Deployable card) throws ConcurrentModificationException {
        graveYard.remove(card);
    }

    public void addToHand(Card card) {
        hand.add(card);

    }

    public void removeFromBattlefield(Deployable card) throws ConcurrentModificationException {
        cardsOnBattleField.remove(card);
    }

    public void addCardToGraveYard(Deployable card) {

        graveYard.add(card);
    }


    public Account getAccount() {
        return account;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public int getMana() {
        return mana;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getNumbereOfFlags() {
        return numbereOfFlags;
    }

    public int getNumberOfTurnsHavingFlag() {
        return numberOfTurnsHavingFlag;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card getNextCard() {
        return nextCard;
    }

    public ArrayList<Deployable> getCardsOnBattleField() {
        return cardsOnBattleField;
    }

    public ArrayList<Deployable> getGraveYard() {
        return graveYard;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public Card getCardInReplace() {
        return cardInReplace;
    }

    public void generateDeckArrangement() {
        currentDeck.shuffle();
    }

    public boolean selectACard(int cardId) {
        for (Card card : hand) {
            if (cardId == card.getId()) {
                selectedCard = card;
                return true;
            }
        }
        for (Deployable card : cardsOnBattleField) {
            if (cardId == card.getUniqueId()) {
                selectedCard = card;
                return true;
            }
        }
        return false;
    }

    public void placeNextCardToHand() {
        if (hand.size() < 6) {
            hand.add(cardInReplace);
            Collections.shuffle(currentDeck.getCards());
            cardInReplace = currentDeck.getCards().get(0);
            currentDeck.getCards().remove(0);
        }
    }

    public boolean doesPlayerHaveDeployable(Deployable card) {
        for (Deployable deployable : cardsOnBattleField) {
            if (deployable.equals(card))
                return true;
        }
        return false;
    }

    public void handleNumberOfTurnHavingFlagAtTheEndOfTurn() {
        if (doesHaveTheFlag()) {
            numberOfTurnsHavingFlag++;
            return;
        }
        numberOfTurnsHavingFlag = 0;
    }

    public boolean doesHaveTheFlag() {
        for (Deployable card : cardsOnBattleField) {
            if (card.hasFlag)
                return true;
        }
        return false;
    }

    public void showNextCard() {
        cardInReplace.show();
    }

    public void endOfTurnBuffsAndFunctions() {

        applyPassiveAndPoisonBuffs();
        buffsChangesAtTheEndOfTurn();

    }


    private void applyPassiveAndPoisonBuffs() {
        for (Deployable card : cardsOnBattleField) {
            for (int i = 0; i < card.functions.size(); i++) {
                if (card.functions.get(i).getFunctionType() == Function.FunctionType.Passive) {
                    battle.compileFunction(card.functions.get(i), card.cell.getX1Coordinate(),
                            card.cell.getX2Coordinate());
                }
            }
            for (int i = 0; i < card.buffs.size(); i++) {
                if (card.buffs.get(i).buffType == Buff.BuffType.Poison) {
                    card.currentHealth--;
                }
            }

        }
    }


    public void buffsChangesAtTheEndOfTurn() {
        for (Deployable card : cardsOnBattleField) {
            for (int i = 0; i < card.buffs.size(); i++) {
                card.buffs.get(i).decreaseTurnsLeft();
                if (!card.buffs.get(i).isContinuous() && card.buffs.get(i).getTurnsLeft() <= 0) {
                    card.buffs.remove(i);
                }
                if (card.buffs.get(i).isContinuous()) {
                    card.buffs.get(i).setActive(true);
                    card.buffs.get(i).setTurnsLeft(2);
                }
            }
        }
    }

    public boolean isSelectedCardDeployed() {
        for (Deployable card : cardsOnBattleField) {
            if (card.equals(selectedCard))
                return true;
        }
        return false;
    }

    public boolean isHeroDead() {
        return getHero().theActualHealth() <= 0;
    }

    public void duplicateTheDeck() {
        for (Card card : account.getTheMainDeck().getCards()) {
            currentDeck.getCards().add(card);
        }
    }
}

