package model;

import constants.CardType;
import javafx.collections.ObservableList;
import view.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private String deckName;
    private ArrayList<Card> cards;
    private Hero hero;
    private Item item;

    public void setItem(Item item) {
        this.item = item;
    }

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
        if (card.getType() == CardType.spell || card.getType() == CardType.minion) {
            if (cards.size() < 18) {
                cards.add(card);
            }
        }
        if (card.getType() == CardType.item) {
            if (item == null) {
                setItem((Item) card);
            }
        }
        if (card.getType() == CardType.hero) {
            if (hero == null) {
                setHero((Hero) card);
            }
        }
    }

    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public void addDisplayableCards(ObservableList<DisplayableCard> displayableCards) {
        for (DisplayableCard displayableCard : displayableCards)
            addDisplayableCard(displayableCard);
    }

    public void addDisplayableCard(DisplayableCard displayableCard) {
        addCard(displayableCard.getCard());
    }

    public void deleteCard(Card card) {
        deleteCardByName(card.getName());
    }

    public void deleteDisplayableCard(DisplayableCard displayablecard) {
        deleteCard(displayablecard.getCard());
    }

    public void deleteDisplayableCards(ObservableList<DisplayableCard> displayableCards) {
        for (DisplayableCard displayableCard : displayableCards) {
            if (!Client.getClient().requestCardDeletion(displayableCard.getCard())){
                System.out.println("Error! card " + displayableCard.getCard().getName() + " not removed!");
                continue;
            }
            deleteDisplayableCard(displayableCard);
        }
    }

    public void deleteCardByName(String cardName) {
        for (Card c : cards) {
            if (c.getName().equalsIgnoreCase(cardName)) {
                cards.remove(c);
                return;
            }
        }
        if (hero.getName().equalsIgnoreCase(cardName)) {
            hero = null;
            return;
        }
        if (item.getName().equalsIgnoreCase(cardName)) {
            item = null;
        }
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Deck(String deckName) {
        this.deckName = deckName;
        this.cards = new ArrayList<>();
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
        Output.print("deck " + deckName + "'s hero and cards :");
        Output.print("Heroes:");
        if (hero != null)
            Output.print(hero.toString());
        Output.print("Items:");
        for (Card card : cards) {
            if (card.type == CardType.item)
                Output.print(card.toString());
        }
        Output.print("Cards:");
        for (Card card : cards) {
            if (card.type != CardType.item && card.type != CardType.hero)
                Output.print(card.toString());
        }
        Output.print("---------------");
    }

    public boolean checkIfValid() {
        if (hero == null)
            return false;
        if (cards.size() != 18)
            return false;
        return numberOfItemsInDeck() <= 1;
    }

}
