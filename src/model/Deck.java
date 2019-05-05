package model;

import constants.CardType;
import view.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private String deckName;
    private ArrayList<Card> cards;
    private Hero hero;
    private Item item;

    public Item getItem() {
        return item;
    }

    public String getDeckName() {
        return deckName;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int numberOfItemsInDeck() {
        int numberOfItem = 0;
        for (Card card : cards) {
            if (card.getType() == CardType.item)
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

    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public Deck(String deckName, Hero hero, Item item) {
        this.deckName = deckName;
        this.hero = hero;
        this.cards = new ArrayList<>();
        this.item = item;
    }

    public Deck(String deckName, ArrayList<Card> cards, Hero hero) {
        this.deckName = deckName;
        this.cards = new ArrayList<Card>(cards);
        this.hero = hero;
    }

    public void show() {
        Output.print("Heroes:\n");
        if (hero != null)
            Output.print(hero.toString());
        Output.print("Items:\n");
        for (Card card : cards) {
            if (card.type == CardType.item)
                Output.print(card.toString());
        }
        Output.print("Cards:\n");
        for (Card card : cards) {
            if (card.type != CardType.item && card.type != CardType.hero)
                Output.print(card.toString());
        }

    }

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int first = random.nextInt(cards.size());
            int second = random.nextInt(cards.size());
            Collections.swap(cards, first, second);
        }
    }

    public boolean checkIfValid() {
        if (hero == null)
            return false;
        if (cards.size() != 18)
            return false;
        return numberOfItemsInDeck() <= 1;

    }

}
