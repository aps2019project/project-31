package model;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SinglePlayer extends BattleManager {

    private static Ai aiPlayer = null;
    private static Account AIAccount = null;
    private static ArrayList<Deck> customGameDecks = new ArrayList<>();
    private static Deck customGameDeck;

    public SinglePlayer(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        super(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
    }

    public static void loadCustomDecks() {
        YaGson yaGson = new YaGsonBuilder().create();
        String path = System.getProperty("user.dir") + "/Sources/StoryDecks/CustomDecks.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            customGameDecks.add(yaGson.fromJson(line, Deck.class));
        } catch (IOException e) {

        }
    }

    public static void setCustomGameDeck(Deck deck) {
        customGameDeck = deck;
    }

    public static Deck getCustomGameDeck() {
        return customGameDeck;
    }

    public static void addCustomDeck(Deck deck) {
        customGameDecks.add(deck);
    }

    public static void makeAIAccount(Deck deck) {
        AIAccount = new Account("AI", "beep", 20000);
        AIAccount.setMainDeck(deck);
        aiPlayer = new Ai(AIAccount);
    }

    public static Player getAiPlayer() {
        return aiPlayer;
    }
}
