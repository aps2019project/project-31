package model;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;

import java.io.*;
import java.util.ArrayList;

public class Story extends SinglePlayer {
    private static Deck firstBattleManagerDeck;
    private static Deck secondBattleManagerDeck;
    private static Deck thirdBattleManagerDeck;

    public Story(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        super(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
    }

    public static void loadStoryDecks(){
        YaGson yaGson = new YaGsonBuilder().create();
        String path = System.getProperty("user.dir") + "/Sources/Story Decks/storyDecks.txt";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
            String line = bufferedReader.readLine();
            firstBattleManagerDeck = yaGson.fromJson(line, Deck.class);
            secondBattleManagerDeck = yaGson.fromJson(bufferedReader.readLine(), Deck.class);
            thirdBattleManagerDeck = yaGson.fromJson(bufferedReader.readLine(), Deck.class);
        }catch (IOException e){

        }
    }





    public static Deck getFirstBattleManagerDeck() {
        return firstBattleManagerDeck;
    }

    public static Deck getSecondBattleManagerDeck() {
        return secondBattleManagerDeck;
    }

    public static Deck getThirdBattleManagerDeck() {
        return thirdBattleManagerDeck;
    }








}
