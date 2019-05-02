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

    public int numberOfItemsInDeck() {
        int numberOfItem = 0;
        for (Card card : cards) {
            if (card.getType() == Card.CardType.item)
                numberOfItem++;
        }
        return numberOfItem;
    }

    public int numberOfCardInDeck(Card card) {
        int numberOfCard = 0;
        for (Card cardd : cards) {
            if (cardd.id == card.id)
                numberOfCard++;
        }
        return numberOfCard;
    }

    public Hero getHero() {
        return hero;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Deck(String deckName) {
        this.deckName = deckName;
        this.cards = new ArrayList<>();
    }

    public Deck(String deckName, Hero hero) {
        this.deckName = deckName;
        this.hero = hero;
        this.cards = new ArrayList<>();
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
    public boolean checkIfValid(){
        if (hero == null)
            return false;
        if (cards.size() != 19)
            return false;
        return numberOfItemsInDeck() <= 1;
    }

}
