
package controller;

import model.Card;
import view.Input;
import view.Output;

import java.util.ArrayList;

public class Shop extends Menu {
    private static ArrayList<Card> allCards;

    public static void goBack() {

    }

    public static void searchCardsByNames(String[] cardNames) {
        for (Card card : allCards) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    Output.showCardIdAndStuff(card);
            }
        }
    }

    public static void searchCardsByNamesInCollection(String[] cardNames) {
        for (Card card : CollectionMenu.getCollection()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    Output.showCardIdAndStuff(card);
            }
        }
    }

    private static void buyCard(Card card) {
        //card not valid
        //not enough money
        //not more than 3 same card
        //bought successfully
    }

    private static void sellCard(Card card) {
        //card not in collection
        //sold successfully
    }

    public static void buyCardsByName(String[] cardNames) {
        for (Card card : allCards) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    buyCard(card);
            }
        }
    }

    public static void sellCardsByName(String[] cardNames) {
        for (Card card : allCards) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    sellCard(card);
            }
        }
    }

    public static void showAllCards() {
        for (Card card : allCards)
            if (card.getType() == Card.CardType.hero)
                card.show();
        for (Card card : allCards)
            if (card.getType() == Card.CardType.item)
                card.show();
        for (Card card : allCards)
            if (card.getType() == Card.CardType.minion)
                card.show();
        for (Card card : allCards)
            if (card.getType() == Card.CardType.spell)
                card.show();
    }

    @Override
    public void run() {
        Input.handleCommandsInShop();
    }

    @Override
    public void show() {

    }

    @Override
    public void help() {

    }
}