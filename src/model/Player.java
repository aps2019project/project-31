package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.regex.*;

public class Player {
    protected Account account;
    protected Deck currentDeck;
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
        this.hand = generateHandArrangement();
        this.currentDeck = new Deck(account.getTheMainDeck().getDeckName(),
                account.getTheMainDeck().getCards(), account.getTheMainDeck().getHero());
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

    public ArrayList<Card> generateHandArrangement() {
        currentDeck.shuffle();
        ArrayList<Card> hand = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(currentDeck.getCards().size());
            hand.add(currentDeck.getCards().get(index));
            currentDeck.getCards().remove(index);
        }
        return hand;

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
        //doOnTurnFunctions();
        applyAtTheEndOfTurnBuffs();
        buffsChangesAtTheEndOfTurn();

    }


    private void applyAtTheEndOfTurnBuffs() {
        for (Deployable card : cardsOnBattleField) {
            for (int i = 0; i < card.buffs.size(); i++) {
                if (card.buffs.get(i).buffType == Buff.BuffType.Poison) {
                    card.currentHealth--;
                }
            }
            for (int i = 0; i < card.functions.size(); i++) {
                if (card.functions.get(i).getFunctionType() == Function.FunctionType.Passive) {
                    battle.compileFunction(card.functions.get(i), card.cell.getX1Coordinate(),
                            card.cell.getX2Coordinate());
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

    public boolean playCard(Card card, int x1, int x2) {
        boolean sit = false;
        switch (card.getType()) {
            case minion:
                sit = battle.playMinion((Minion) card, x1, x2);
                break;
            case spell:
                sit = battle.playSpell((Spell) card, x1, x2);
                break;
            case item:
                sit = battle.useItem((Item) card, x1, x2);
                break;
        }
        return sit;
    }

    private void handleCommands(String input) {
        Pattern pattern = Pattern.compile("\\s*show my minions\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            for (Card card : cardsOnBattleField) {
                card.show();
            }
            return;
        }
        pattern = Pattern.compile("\\s*show opponent minions\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            Player opponent = battle.getOtherPlayer();
            for (Card card : opponent.cardsOnBattleField) {
                card.show();
            }
            opponent.currentDeck.getHero().show();
            return;
        }
        pattern = Pattern.compile("\\s*show card info\\s+(\\d+)");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            for (Card card : cardsOnBattleField) {
                if (card.getId() == Integer.valueOf(matcher.group(1))) {
                    card.showCardInfo();
                }
            }
        }
        pattern = Pattern.compile("\\s*select (\\d+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            selectACard(Integer.valueOf(matcher.group(1)));
        }

        pattern = Pattern.compile("\\s*move to\\((\\d),(\\d)\\)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            if (selectedCard.isDeployed()) {
                if (selectedCard.getType().equals("hero") || selectedCard.getType().equals("minion")) {
                    int x1 = Integer.valueOf(matcher.group(1)), x2 = Integer.valueOf(matcher.group(2));

                }

            }
        }
        pattern = Pattern.compile("\\s*attack (\\d+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            if (selectedCard.isDeployed() && (selectedCard.getType().equals("hero") ||
                    selectedCard.getType().equals("minion"))) {

            }
        }
        pattern = Pattern.compile("\\s*attack combo (\\d+)\\s+((\\d+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {

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
}

