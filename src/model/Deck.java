package model;

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

    public Deck(String deckName, Hero hero) {
        this.deckName = deckName;
        this.hero = hero;
        this.cards = new ArrayList<Card>();
    }

    public Deck(String deckName, ArrayList<Card> cards, Hero hero) {
        this.deckName = deckName;
        this.cards = new ArrayList<Card>(cards);
        this.hero = hero;
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int first = random.nextInt(cards.size());
            int second = random.nextInt(cards.size());
            Collections.swap(cards, first, second);
        }
    }

}
