package model;

import constants.FunctionType;
import view.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;

public class Player {
    protected Account account;
    protected Deck currentDeck;
    protected int mana;
    protected int remainingTime;
    protected int numberOfFlags;
    protected int numberOfTurnsHavingFlag;
    protected ArrayList<Card> hand;
    protected Card nextCard;
    protected ArrayList<Deployable> cardsOnBattleField;
    protected ArrayList<Deployable> graveYard;
    protected Card selectedCard;
    protected Card cardInReplace;
    protected BattleManager battle;
    private boolean isAi;
    protected int[] manaChangerInTurn = new int[40];
    private static int howFuckedUpIAm = 0;

    public Player(Account account, boolean isAi) {
        this.account = account;
        this.numberOfFlags = 0;
        this.numberOfTurnsHavingFlag = 0;
        this.hand = new ArrayList<>();
        this.cardsOnBattleField = new ArrayList<>();
        this.graveYard = new ArrayList<>();
        try {
            this.currentDeck = new Deck("temp: " + account.getTheMainDeck().getDeckName(),
                    account.getTheMainDeck().getHero().duplicateDeployed(battle, account),
                    account.getTheMainDeck().getItem());
        } catch (Exception e) {
            this.currentDeck = new Deck("temp: " + account.getTheMainDeck().getDeckName(),
                    account.getTheMainDeck().getHero().duplicateDeployed(account, howFuckedUpIAm++),
                    account.getTheMainDeck().getItem());
        }
        this.isAi = isAi;

    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public void setBattle(BattleManager battle) {
        this.battle = battle;
    }

    public boolean isAi() {
        return isAi;
    }


    public Hero getHero() {
        return currentDeck.getHero();
    }

    public void decreaseManaInTheTurn(int turn, int addMana) {
        manaChangerInTurn[turn] += addMana;
    }

    public void increaseManaInTheTurn(int turn, int addMana) {
        manaChangerInTurn[turn] += addMana;
    }

    public int[] getManaChangerInTurn() {
        return manaChangerInTurn;
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

    public int getNumberOfFlags() {
        return numberOfFlags;
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

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
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
        Output.print(cardInReplace.toString());
    }

    public void endOfTurnBuffsAndFunctions() {

        applyPassiveAndPoisonBuffs();
        buffsChangesAtTheEndOfTurn();

    }


    private void applyPassiveAndPoisonBuffs() {
        for (Deployable card : cardsOnBattleField) {
            for (int i = 0; i < card.functions.size(); i++) {
                if (card.functions.get(i).getFunctionType() == FunctionType.Passive) {
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

    public void showHand() {
        for (Card card : hand) {
            card.show();
        }
    }

    public void showCollectibleItems() {
        for (Deployable card : cardsOnBattleField) {
            if (card.getItem() != null)
                card.getItem().show();
        }
    }
}

