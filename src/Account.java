import java.util.ArrayList;

public class Account {
    private ArrayList<Card> collection;
    private int daric;
    private ArrayList<Deck> decks;
    private Deck theMainDeck;
    private ArrayList<Match> matchHistories;

    public ArrayList<Card> getCollection() {
        return collection;
    }

    public int getDaric() {
        return daric;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public Deck getTheMainDeck() {
        return theMainDeck;
    }

    public ArrayList<Match> getMatchHistories() {
        return matchHistories;
    }
}
