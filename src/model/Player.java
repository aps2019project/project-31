package model;

import constants.CardType;
import constants.FunctionType;
import controller.BattlePageController;
import view.Output;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Random;

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
    protected ArrayList<DisplayableDeployable> graveYard;
    protected Card selectedCard;
    protected Card cardInReplace;
    protected BattleManager battle;
    private boolean isAi;
    protected int[] manaChangerInTurn = new int[200];
    private static int howFuckedUpIAm = 0;

    public Player(Account account, boolean isAi) {
        this.account = account;
        this.numberOfFlags = 0;
        this.numberOfTurnsHavingFlag = 0;
        this.hand = new ArrayList<>();
        this.cardsOnBattleField = new ArrayList<>();
        this.graveYard = new ArrayList<>();
        try {
            if (account.getTheMainDeck().getItem() == null) {
                this.currentDeck = new Deck("temp: " + account.getTheMainDeck().getDeckName(),
                        account.getTheMainDeck().getHero().duplicateDeployed(account, howFuckedUpIAm++),
                        null);
            } else {
                this.currentDeck = new Deck("temp: " + account.getTheMainDeck().getDeckName(),
                        account.getTheMainDeck().getHero().duplicateDeployed(account, howFuckedUpIAm++),
                        account.getTheMainDeck().getItem());
            }
            System.out.println("the current deck is MADE");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("currentDeck is null (the account.getMainDeck() is null)");
        }
        this.isAi = isAi;

    }

    public int handSize() {
        int i = 0;
        for (Card card : hand) {
            if (card != null)
                i++;
        }
        return i;
    }

    public int deckSize() {
        int i = 0;
        for (Card card : getCurrentDeck().getCards()) {
            if (card != null)
                i++;
        }
        return i;
    }

    public void decreaseMana(int manaCost) {
        mana -= manaCost;
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
        if (card != null)
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

    public void addCardToGraveYard(DisplayableDeployable card) {

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
        if (nextCard != null)
            return nextCard;
        else {
            System.err.println("next card is null, we are returning null !");
            return null;
        }
    }

    public ArrayList<Deployable> getCardsOnBattleField() {
        return cardsOnBattleField;
    }

    public ArrayList<DisplayableDeployable> getGraveYard() {
        return graveYard;
    }

    public Card getSelectedCard() {
        if (selectedCard != null)
            return selectedCard;
        else {
            System.err.println("selected card is null, we are returning null !");
            return null;
        }
    }

    public Card getCardInReplace() {
        if (cardInReplace != null)
            return cardInReplace;
        else {
            System.err.println("card in replace is null, we are returning null !");
            return null;
        }
    }

    public void generateDeckArrangement() {
        currentDeck.shuffle();
    }

    public void setSelectedCard(Card selectedCard) {
        System.err.println("selected " + selectedCard.getName() + " successfully!");
        this.selectedCard = selectedCard;
    }

    public Card cardInHand(int cardId) {
        for (Card card : hand) {
            if (card != null && cardId == card.getId())
                return card;
        }
        return null;
    }

    public Deployable myDeployable(int uniqueId) {
        for (Deployable card : cardsOnBattleField) {
            if (card != null && uniqueId == card.getUniqueId())
                return card;
        }
        return null;
    }

    public Deployable opponentDeployable(int uniqueId) {
        for (Deployable card : BattlePageController.getInstance().getOpponent().cardsOnBattleField) {
            if (card != null && uniqueId == card.getUniqueId())
                return card;
        }
        return null;
    }



    public boolean selectACard(int cardId) {
        if (cardInHand(cardId) != null) {
            System.err.println("selected card successfully");
            selectedCard = cardInHand(cardId);
            return true;
        }
        if (myDeployable(cardId) != null) {
            System.err.println("selected card successfully");
            selectedCard = cardInHand(cardId);
            return true;
        }
        if (battle.getCurrentPlayer().getHero().getId() == cardId) {
            System.err.println("selected card successfully");
            selectedCard = battle.getCurrentPlayer().getHero();
            return true;
        }
        System.err.println("card not selected");
        return false;
    }

    public void placeNextCardToHand() {
        if (nextCard != null && hand.size() < 6) {
            hand.add(nextCard);
        }
        Collections.shuffle(currentDeck.getCards());
        if (currentDeck.getCards().size() > 0) {
            nextCard = currentDeck.getCards().get(0);
            currentDeck.getCards().remove(0);
        }
    }

    public boolean doesPlayerHaveDeployable(Deployable card) {
        for (Deployable deployable : cardsOnBattleField) {
            if (deployable != null && deployable.equals(card))
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
            if (card != null && card.hasFlag)
                return true;
        }
        return false;
    }

    public void showNextCard() {
        Output.print(nextCard.toString());
    }

    public void endOfTurnBuffsAndFunctions() {
        applyPassiveAndPoisonBuffs();
        buffsChangesAtTheEndOfTurn();

    }


    private void applyPassiveAndPoisonBuffs() {
        for (Deployable card : cardsOnBattleField) {
            if (card == null)
                continue;
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
                if (card == null)
                    break;
                card.buffs.get(i).decreaseTurnsLeft();
                if (!card.buffs.get(i).isContinuous() && card.buffs.get(i).getTurnsLeft() <= 0) {
                    card.buffs.remove(i);
                    continue;
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
            if (card != null && card.equals(selectedCard))
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
            if (card != null)
                card.show();
        }
    }

    public void showCollectibleItems() {
        for (Deployable card : cardsOnBattleField) {
            if (card.getItem() != null)
                card.getItem().show();
        }
    }

    public void generateCardInReplace() {
        Random random = new Random();
        int value = random.nextInt(getCurrentDeck().getCards().size());
        if (currentDeck.getCards().get(value).equals(nextCard))
            generateCardInReplace();
        cardInReplace = currentDeck.getCards().get(value);
    }
}

