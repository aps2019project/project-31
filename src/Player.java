import java.util.ArrayList;
import java.util.Random;

public class Player {
    private Account account;
    private Deck currentDeck;
    private int mana;
    private int remainingTime;
    private int numbereOfFlags;
    private int numberOfTurnsHavingFlag;
    private ArrayList<Card> hand;
    private Card nextCard;
    private ArrayList<CardInGame> cardsOnBattleField;
    private ArrayList<CardInGame> graveYard;
    private Card selectedCard;
    private Card cardInReplace;

    public Player(Account account) {
        this.account = account;
        this.currentDeck = account.getTheMainDeck();
        this.hand = generateHandArrangement();
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

    public ArrayList<CardInGame> getCardsOnBattleField() {
        return cardsOnBattleField;
    }

    public ArrayList<CardInGame> getGraveYard() {
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

    public Card generateReplaceCard() {

    }

    public boolean selectACard() {

    }

    public void useHeroSpecialPower() {

    }

    public void showHand() {
        for (Card i : hand) {
            System.out.println(i.toString());
        }
    }

    public void playCard(Card card) {

    }

    public void placeNextCardToHand() {
        if (hand.size() < 6) {
            hand.add(nextCard);
            generateNextCard();
        }
    }

    private void generateNextCard() {
        Random random = new Random();

    }
}
