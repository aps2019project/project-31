package model;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.regex.*;

public class Player {
    private Account account;
    private Deck currentDeck;
    private int mana;
    private int remainingTime;
    private int numbereOfFlags;
    private int numberOfTurnsHavingFlag;
    private ArrayList<Card> hand;
    private Card nextCard;
    private ArrayList<Card> cardsOnBattleField;
    private ArrayList<Card> graveYard;
    private Card selectedCard;
    private Card cardInReplace;
    private BattleManager battle;

    public Player(Account account) {
        this.account = account;
        this.hand = generateHandArrangement();
        this.currentDeck = new Deck(account.getTheMainDeck().getDeckName(),
                account.getTheMainDeck().getCards(), account.getTheMainDeck().getHero());
    }

    public void addCardToBattlefield(Card card) {
        cardsOnBattleField.add(card);
    }

    public void removeFromHand(Card card) throws ConcurrentModificationException {
        hand.remove(card);
    }

    public void removeFromGraveYard(Card card) throws ConcurrentModificationException{
        graveYard.remove(card);
    }

    public void addToHand(Card card){
        hand.add(card);

    }

    public void removeFromBattlefield(Card card) throws ConcurrentModificationException{
        cardsOnBattleField.remove(card);
    }

    public void addCardToGraveYard(Card card){

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

    public ArrayList<Card> getCardsOnBattleField() {
        return cardsOnBattleField;
    }

    public ArrayList<Card> getGraveYard() {
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
        for (Card card : cardsOnBattleField) {
            if (cardId == card.getId()) {
                selectedCard = card;
                return true;
            }
        }
        return false;
    }

    public void generateReplaceCard() {
        Random random = new Random();
        int index = random.nextInt(currentDeck.getCards().size());
        cardInReplace = currentDeck.getCards().get(index);
    }

    public void placeNextCardToHand() {
        if (hand.size() < 6) {
            hand.add(cardInReplace);
            generateReplaceCard();
        }
    }

    public void showNextCard() {
        cardInReplace.show();
    }

    public void runTheTurn() {
        remainingTime = 60;
        while (true) {
            String input = Scanners.getScanner().nextLine();
            handleCommands(input);
            if (input.equals("end turn") || remainingTime <= 0)
                break;
        }
        placeNextCardToHand();
    }

    public void endTurn() {

    }

    public void showHand() {
        for (Card i : hand) {
            System.out.println(i.toString());
        }
    }

    public void playCard(Card card, int x, int y) {
        switch (card.getType()) {
            case minion:
                battle.playMinion((Minion) card, x, y);
                break;
            case spell:
                battle.playSpell((Spell) card, x, y);
                break;
            case item:
                battle.useItem((Item) card, x, y);

        }

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
            Player opponent = battle.getOtherPlayer(account.getUsername());
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
                    int x = Integer.valueOf(matcher.group(1)), y = Integer.valueOf(matcher.group(2));
                    Cell tempCell = new Cell(x, y);
                    ((MoveableDeployed) selectedCard).move(tempCell);
                }

            }
        }
        pattern = Pattern.compile("\\s*attack (\\d+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {
            if (selectedCard.isDeployed() && (selectedCard.getType().equals("hero") ||
                    selectedCard.getType().equals("minion"))) {
                Cell destinationCell = Map.findCellByCardId(Integer.valueOf(matcher.group(1)));
                ((MoveableDeployed) selectedCard).attack(destinationCell);
            }
        }
        pattern = Pattern.compile("\\s*attack combo (\\d+)\\s+((\\d+\\s*)+)\\s*");
        matcher = pattern.matcher(input);
        if (matcher.matches()) {

        }
    }
}
