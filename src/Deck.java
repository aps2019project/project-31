import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private String deckName;
    private ArrayList<Card> cards;
    private Hero hero;

    public String getDeckName() {
        return deckName;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Hero getHero() {
        return hero;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int first = random.nextInt(cards.size());
            int second = random.nextInt(cards.size());
            Collections.swap(cards, first, second);
        }
    }

    public Deck getDuplicateDeck() {

    }
}
