package constants;

import java.util.ArrayList;
import java.util.Arrays;

public enum CardType {
    hero,
    item,
    minion,
    spell,
    herospell;

    public static ArrayList<CardType> getAll() {
        return new ArrayList<>(Arrays.asList(hero, item, minion, spell));
    }
}
