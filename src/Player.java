import java.util.ArrayList;

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
        this.hand = generateDeckArrangement();
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
}
