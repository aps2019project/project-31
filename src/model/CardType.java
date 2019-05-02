package model;

import java.util.ArrayList;
import java.util.Arrays;

public enum CardType {
    minion,
    hero,
    spell,
    item,
    herospell;

    public ArrayList<CardType> getAll(){
        return new ArrayList<>(Arrays.asList(minion, hero, spell, item));
    }
}
