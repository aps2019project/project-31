package model;

import java.util.ArrayList;

public class Story extends SinglePlayer {
    public Story(Map map, String gameMode, Player currentPlayer, Player playerOne, Player aiPlayer, ArrayList<Deck> decks) {
        super(map, gameMode, currentPlayer, playerOne, aiPlayer);
        this.decks = decks;
    }

    private ArrayList<Deck> decks;

}
