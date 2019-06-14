
package controller;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.CardType;
import model.Card;
import model.Minion;
import model.*;
import view.Input;
import view.Output;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Shop extends Menu {
    private static ArrayList<Card> allCards = new ArrayList<>();
    private static ArrayList<Minion> allMinions = new ArrayList<>();
    private static ArrayList<Spell> allSpells = new ArrayList<>();
    private static ArrayList<Item> allCollectibles = new ArrayList<>();
    private static ArrayList<Hero> allHeroes = new ArrayList<>();
    private static ArrayList<Item> allUsables = new ArrayList<>();

    public static void loadAllCards() {
        YaGson yaGson = new YaGsonBuilder().create();
        System.err.println("loading minions...");
        String path = System.getProperty("user.dir") + "/Sources/Cards/Minions.txt";
        loadMinions(yaGson, path);

        System.err.println("loading spells...");
        path = System.getProperty("user.dir") + "/Sources/Cards/Spells.txt";
        loadSpells(yaGson, path);

        System.err.println("loading heroes...");
        path = System.getProperty("user.dir") + "/Sources/Cards/Heroes.txt";
        loadHeroes(yaGson, path);

        System.err.println("loading collectibles...");
        path = System.getProperty("user.dir") + "/Sources/Cards/Collectible_Items.txt";
        loadItems(yaGson, path, allCollectibles);

        System.err.println("loading usables...");
        path = System.getProperty("user.dir") + "/Sources/Cards/Usable_Items.txt";
        loadItems(yaGson, path, allUsables);
        allCards.addAll(allUsables);
    }

    public static ArrayList<Spell> getAllSpells() {
        return allSpells;
    }

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public static ArrayList<Minion> getAllMinions() {
        return allMinions;
    }

    public static ArrayList<Hero> getAllHeroes() {
        return allHeroes;
    }

    public static ArrayList<Item> getAllUsables() {
        return allUsables;
    }

    private static void loadHeroes(YaGson yaGson, String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                allCards.add(yaGson.fromJson(line, Hero.class));
                allHeroes.add(yaGson.fromJson(line, Hero.class));
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            // exception handling
            System.err.println("File not found error");
        } catch (IOException e) {
            // exception handling
            System.err.println("File error");
        }
    }

    private static void loadSpells(YaGson yaGson, String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                allCards.add(yaGson.fromJson(line, Spell.class));
                allSpells.add(yaGson.fromJson(line, Spell.class));
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            // exception handling
            System.err.println("File not found error");
        } catch (IOException e) {
            // exception handling
            System.err.println("File error");
        }
    }

    public static ArrayList<Item> getAllCollectibles() {
        return allCollectibles;
    }

    private static void loadMinions(YaGson yaGson, String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                allCards.add(yaGson.fromJson(line, Minion.class));
                allMinions.add(yaGson.fromJson(line, Minion.class));
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            // exception handling
            System.err.println("File not found error");
        } catch (IOException e) {
            // exception handling
            System.err.println("File error");
        }
    }

    private static void loadItems(YaGson yaGson, String path, ArrayList<Item> items) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                //items not added to cards
                items.add(yaGson.fromJson(line, Item.class));
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            // exception handling
            System.err.println("File not found error");
        } catch (IOException e) {
            // exception handling
            System.err.println("File error");
        }
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
        for (Card card : Account.getMainAccount().getCollection()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    Output.showCardIdAndStuff(card);
            }
        }
    }

    private static void buyCard(Card card) {
        if (Account.getMainAccount().getDaric() < card.getPrice()) {
            Output.print("not enough money");
            return;
        } else {
            int numberOfCards = 0;
            for (Card c : Account.getMainAccount().getCollection()) {
                if (card.getName().equals(c.getName()))
                    numberOfCards++;
            }
            if (numberOfCards >= 3) {
                Output.print("Not more than 3 cards");
                return;
            }
        }
        Account.getMainAccount().decreaseDaric(card.getPrice());
        Account.getMainAccount().getCollection().add(card);
        Output.print("card :" + card.getName() + " bought successfully");
    }

    private static void sellCard(Card card) {
        Card theCard = ShopController.getInstance().findCardInCollection(card);
        if (theCard == null) {
            Output.print("card not in collection");
            return;
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            try {
                deck.getCards().remove(theCard);
            } catch (Exception e) {

            }
        }
        Account.getMainAccount().addDaric(card.getPrice());
        Account.getMainAccount().getCollection().remove(theCard);
        Output.print("sold successfully");
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
        for (CardType cardType : CardType.getAll()) {
            for (Card card : allCards) {
                if (card.getType() == cardType)
                    card.show();
            }
        }
    }
}