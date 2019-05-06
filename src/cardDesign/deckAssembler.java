package cardDesign;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class deckAssembler {
    private static ArrayList<Card> allCards = new ArrayList<>();
    private static ArrayList<Minion> allMinions = new ArrayList<>();
    private static ArrayList<Spell> allSpells = new ArrayList<>();
    private static ArrayList<Item> allCollectibles = new ArrayList<>();
    private static ArrayList<Hero> allHeroes = new ArrayList<>();
    private static ArrayList<Item> allUsables = new ArrayList<>();

    private static void loadAllCards() {
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
    public static void main(String[] args) {
        loadAllCards();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter deck name");
        String name = scanner.nextLine();
        for (Item item: allUsables){
            System.out.println(item.getId() + ": " + item.getName());
        }
        System.out.println("Enter item ID:");
        int itemNum = scanner.nextInt();
        Item item = null;
        for (Item item1: allUsables){
            if (item1.getId() == itemNum)
            {
                item = item1;
                System.out.println("okay");
            }
        }
        for (Hero hero: allHeroes){
            System.out.println(hero.getId() + ": " + hero.getName());
        }
        System.out.println("Enter hero ID:");
        int heroNum = scanner.nextInt();
        Hero hero = null;
        for (Hero hero1: allHeroes){
            if (hero1.getId() == heroNum)
            {
                hero = hero1;
                System.out.println("okay");
            }
        }

        Deck newDeck = new Deck(name, hero, item);
        System.out.println("Enter card ID and then -1 to exit:");
        int num = scanner.nextInt();
        while (num != -1){
            for (Card card: allCards){
                if (card.getId() == num){
                    newDeck.addCard(card);
                    System.out.println(card.getId() + " " + card.getName() + "added");
                }
            }
            System.out.println("Enter next card ID:");
            num = scanner.nextInt();
        }
        YaGson yaGson = new YaGsonBuilder().create();
        String path = System.getProperty("user.dir") + "/Sources/StoryDecks/storyDecks.txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path,true))){
            bufferedWriter.write(yaGson.toJson(newDeck) + "\n");
        }catch (IOException e){

        }


    }
}
