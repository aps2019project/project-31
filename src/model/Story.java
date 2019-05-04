package model;

import constants.GameMode;

import java.util.ArrayList;

public class Story extends SinglePlayer {

    public Story(int id, String title, ArrayList<Deck> decks) {
        super(id, title);
        this.decks = decks;
    }


    private ArrayList<Deck> decks;

}
