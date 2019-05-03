package controller;

import model.Account;
import model.Card;
import model.Deck;
import model.Hero;
import view.Input;
import view.Output;

import java.util.ArrayList;
import java.util.Calendar;

public class CollectionMenu {
    private static ArrayList<Card> collection = new ArrayList<>();
    private static Deck editingDeck;
    private static Account account;

    public static void createDeck(String name) {
        Deck deck = new Deck(name);
        account.getDecks().add(deck);
        editingDeck = deck;
    }

    private static void addCardToDeck(Card card) {
        if (editingDeck.getCards().size() < 19 && editingDeck.numberOfCardInDeck(card) < 3) {
            editingDeck.addCard(card);
        }
        if (card.getType() == Card.CardType.hero && editingDeck.getHero() == null) {
            editingDeck.setHero((Hero) card);
        }
    }

    public static void addCardsToDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        for (String number : numbers) {
            Card card = findCardById(Integer.valueOf(number));
            if (card != null)
                addCardToDeck(card);
        }

    }

    public static void removeCardsFromDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        for (String number : numbers) {
            Card card = findCardById(Integer.valueOf(number));
            if (card != null) {
                try {
                    editingDeck.getCards().remove(card);
                } catch (Exception e) {
                    Output.notInDeck();
                }
                if (card.getType() == Card.CardType.hero)
                    editingDeck.setHero(null);
            }

        }
    }

    public static void selectAsMainDeck(String deckName) {
        selectDeck(deckName);
        checkValidationOfDeck(deckName);
        if (editingDeck.checkIfValid())
            account.setTheMainDeck(editingDeck);
    }

    public static void showAllDecks() {
        account.getTheMainDeck().show();
        for (Deck deck : account.getDecks()) {
            if (deck != account.getTheMainDeck())
                deck.show();
        }
    }

    public static void showDeckByName(String deckName) {
        selectDeck(deckName);
        editingDeck.show();
    }


    public static void checkValidationOfDeck(String deckName) {
        selectDeck(deckName);
        Output.showValidationOfDeck(editingDeck.checkIfValid());
    }

    private static Card findCardById(int cardId) {
        for (Card card : collection) {
            if (cardId == card.getId())
                return card;
        }
        return null;
    }

    public static void showCardsByNames(String[] names) {
        for (Card card : collection) {
            for (String name : names) {
                if (card.getName().equals(name))
                    card.show();
            }
        }
    }

    public static void deleteDeck(String deckName) {
        if (findDeckByName(deckName) == null) {
            Output.thereIsntDeck();
            return;
        }
        account.getDecks().remove(findDeckByName(deckName));
    }

    public static void selectDeck(String deckName) {
        if (findDeckByName(deckName) == null) {
            Output.thereIsntDeck();
            return;
        }
        editingDeck = findDeckByName(deckName);
    }

    private static Deck findDeckByName(String deckName) {
        for (Deck deck : account.getDecks()) {
            if (deck.getDeckName().equals(deckName))
                return deck;
        }
        return null;
    }

    public static void setAccount(Account account) {
        CollectionMenu.account = account;
    }

    public void run() {
        Input.handleCommandsInCollectionMenu();
    }

    public static void back() {

    }


    public static ArrayList<Card> getCollection() {
        return collection;
    }

    public static void showAllMyCards() {

    }

    public static void saveAndGoBack() {


        back();
    }

}