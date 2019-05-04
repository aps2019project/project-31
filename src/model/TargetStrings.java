package model;

import java.util.ArrayList;
import java.util.Arrays;

public class TargetStrings {
    public static final String ALL_ENEMIES = "all enemies";
    public static final String ALL_ENEMY_MINIONS = "all enemy minions";
    public static final String ALL_ENEMIES_IN_ROW = "row enemies";
    public static final String ALL_ENEMIES_IN_COLUMN = "column enemies";
    public static final String ENEMY_MINION = "enemy minion";
    public static final String ALL_ALLIES = "all allies";
    public static final String ALLY = "ally";
    public static final String ANY_UNIT = "any unit";
    public static final String SQUARE = "square";
    public static final String ATTACK_TARGET = "attack target";
    public static final String SURROUNDING_ENEMY_MINIONS = "surrounding enemy minions";
    public static final String MINIONS_WITH_DISTANCE = "minions with distance";
    public static final String SURROUNDING_ALLIED_MINIONS = "surrounding allied minions";
    public static final String SELF = "self";
    public static final String ENEMY_HERO = "enemy hero";
    public static final String RANDOM_SURROUNDING_ENEMY_MINION = "random enemy nearby";
    public static final String RANDOM_ENEMY_MINION = "random enemy minion";
    public static final String ALLIED_GENERAL_ROW = "allied general row";
    public static final String ENEMY = "enemy";
    public static final String ALLIED_HERO = "allied hero";
    public static final String ALLIED_MINION = "allied minion";
    public static final String ALL_ALLIED_MINIONS = "all allied minions";
    public static final String RANDOM_RANGED_HYBRID = "random ranged or hybrid unit";
    public static final String ENEMY_GENERAL_RANGED_HYBRID = "enemy general if it's ranged or hybrid";
    public static final String ALLIED_GENERAL_RANGED_HYBRID = "allied general if it's ranged or hybrid";
    public static final String RANDOM_UNIT = "a random unit";
    public static final String RANDOM_MINION = "a random minion";
    public static final String ALL_MELEE_UNITS = "all melee units";
    public static final String RANDOM_ALLIED_UNIT = "random allied unit";
    public static final String RANDOM_ENEMY_UNIT ="random enemy unit";

    public static ArrayList<String> allTargets(){
        return new ArrayList<>(Arrays.asList(ALL_ENEMIES, ALL_ENEMY_MINIONS, ALL_ENEMIES_IN_ROW,ALL_ENEMIES_IN_COLUMN,
                ENEMY_MINION, ALL_ALLIES, ALLY, ANY_UNIT, SQUARE, ATTACK_TARGET, SURROUNDING_ENEMY_MINIONS,
                MINIONS_WITH_DISTANCE, SURROUNDING_ALLIED_MINIONS, SELF, ENEMY_HERO, RANDOM_SURROUNDING_ENEMY_MINION,
                RANDOM_ENEMY_MINION, ALLIED_GENERAL_ROW, ENEMY, ALLIED_HERO, ALLIED_MINION, ALL_ALLIED_MINIONS,
                RANDOM_RANGED_HYBRID, ENEMY_GENERAL_RANGED_HYBRID,
                ALLIED_GENERAL_RANGED_HYBRID, RANDOM_UNIT,RANDOM_MINION, ALL_MELEE_UNITS,
                RANDOM_ALLIED_UNIT, RANDOM_ENEMY_UNIT));
    }

}
