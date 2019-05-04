package model;

import constants.GameMode;

import java.util.ArrayList;

public class Story extends SinglePlayer {


    private ArrayList<Deck> decks;

    public Story(Map map, GameMode gameMode, Player currentPlayer, int maxNumberOfFlags) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags);
    }
}
