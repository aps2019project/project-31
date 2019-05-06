package controller;

import model.Account;
import model.Card;
import model.Deck;
import view.Output;

public class CollectionMenu extends Menu {
    public static void showAllDecks() {
        try {
            Account.getMainAccount().getTheMainDeck().show();
        } catch (NullPointerException e) {
            System.err.println("main deck not initialized yet");
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            if (deck != Account.getMainAccount().getTheMainDeck())
                deck.show();
        }
    }

    public static Card findCardByIdInCollection(int cardId) {
        for (Card card : Account.getMainAccount().getCollection()) {
            if (card != null && cardId == card.getId())
                return card;
        }
        return null;
    }

    public static void showCardsByNames(String[] names) {
        for (Card card : Account.getMainAccount().getCollection()) {
            for (String name : names) {
                if (card != null && card.getName().equals(name))
                    card.show();
            }
        }
    }

    public static void showAllMyCards() {
        for (Card card : Account.getMainAccount().getCollection()) {
            Output.print(card.toString());
        }
    }

    public static void saveAndGoBack() {

    }
}