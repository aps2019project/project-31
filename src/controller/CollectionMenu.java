package controller;

import constants.CardType;
import model.Account;
import model.Card;
import model.Deck;
import model.Hero;
import view.Output;

import java.util.ArrayList;

public class CollectionMenu extends Menu {
    private static ArrayList<Card> collection = new ArrayList<>();
    private static Deck editingDeck;

    public static void createDeck(String name) {
        Deck deck = new Deck(name);
        Account.getMainAccount().addDeck(deck);
        editingDeck = deck;
    }

    private static void addCardToDeck(Card card) {
        if (editingDeck.getCards().size() < 19 && editingDeck.numberOfCardInDeck(card) < 3) {
            editingDeck.addCard(card);
        }
        if (card.getType() == CardType.hero && editingDeck.getHero() == null) {
            editingDeck.setHero((Hero) card);
        }
    }

    public static void addCardsToDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        for (String number : numbers) {
            Card card = findCardByIdInCollection(Integer.valueOf(number));
            if (card != null)
                addCardToDeck(card);
            else{
                Output.print("card :" + number + " is not in your collection");
            }
        }
    }

    public static void removeCardsFromDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        for (String number : numbers) {
            Card card = findCardByIdInCollection(Integer.valueOf(number));
            if (card != null) {
                try {
                    editingDeck.getCards().remove(card);
                } catch (Exception e) {
                    Output.notInDeck();
                }
                if (card.getType() == CardType.hero)
                    editingDeck.setHero(null);
            }

        }
    }

    public static void selectAsMainDeck(String deckName) {
        selectDeck(deckName);
        checkValidationOfDeck(deckName);
        if (editingDeck.checkIfValid())
            Account.getMainAccount().setTheMainDeck(editingDeck);
    }

    public static void showAllDecks() {
       try {
           Account.getMainAccount().getTheMainDeck().show();
       }catch (NullPointerException e){
           System.err.println("main deck not initialized yet");
       }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            if (deck != Account.getMainAccount().getTheMainDeck())
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

    private static Card findCardByIdInCollection(int cardId) {
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
        Account.getMainAccount().getDecks().remove(findDeckByName(deckName));
    }

    public static void selectDeck(String deckName) {
        if (findDeckByName(deckName) == null) {
            Output.thereIsntDeck();
            return;
        }
        editingDeck = findDeckByName(deckName);
    }

    private static Deck findDeckByName(String deckName) {
        for (Deck deck : Account.getMainAccount().getDecks()) {
            if (deck.getDeckName().equals(deckName))
                return deck;
        }
        return null;
    }

    public static ArrayList<Card> getCollection() {
        return collection;
    }

    public static void showAllMyCards() {
        for (Card card: Account.getMainAccount().getCollection()) {
            Output.print(card.toString());
        }
    }

    public static void saveAndGoBack() {

    }
}