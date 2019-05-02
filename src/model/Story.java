package model;

import constants.GameMode;

import java.util.ArrayList;

public class Story extends SinglePlayer {
    public Story(Map map, GameMode gameMode, Player currentPlayer, int maxNumberOfFlags, Player playerOne, Player aiPlayer, ArrayList<Deck> decks) {
        super(map, gameMode, currentPlayer, maxNumberOfFlags, playerOne, aiPlayer);
        this.decks = decks;
    }

    private ArrayList<Deck> decks;

}
