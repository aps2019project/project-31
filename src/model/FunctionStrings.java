package model;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionStrings {
    public static final String STUN = "stun target for";
    public static final String ACCUMULATING_ATTACKS = "accumulate previous attacks on target";
    public static final String DISARM = "disarm target for";
    public static final String APPLY_POISON = "poison target for";
    public static final String DEAL_DAMAGE = "deal damage";
    public static final String IGNORE_HOLYBUFF = "ignore holybuff";
    public static final String APPLY_CONDITION = "apply condition";
    public static final String APPLY_BUFF = "apply buff";
    public static final String REVERT_DISARM = "revert disarm";
    public static final String REMOVE_POISON = "remove poison";
    public static final String REMOVE_DEBUFFS = "remove debuffs";
    public static final String IGNORE_ATTACK = "ignore attack";
    public static final String IGNORE_LESSER_ATTACK = "ignore lesser attacks";
    public static final String REMOVE_BENEFICIALS = "remove beneficial effects";
    public static final String DISPEL = "dispels target";
    public static final String INCREASE_ATTACK = "increase attack";
    public static final String POISON_CELL = "poison target cells";
    public static final String SET_ON_FIRE = "set target cell on fire";
    public static final String KILL_TARGETS = "straight up murder fools";
    public static final String INDISARMABLE = "indisarmable";
    public static final String UNPOISONABLE = "unpoisonable";
    public static final String BLEED = "bleed";
    public static final String HEAL = "heal";
    public static final String INVULNERABLE = "invulnerable";
    public static final String HOLY_CELL = "holy cell";
    public static final String GIVE_FUNCTION = "give function";

    public static ArrayList<String> allFunctionStrings() {
        return new ArrayList<>(Arrays.asList(STUN, ACCUMULATING_ATTACKS, DEAL_DAMAGE,
                IGNORE_HOLYBUFF, APPLY_BUFF, IGNORE_LESSER_ATTACK, DISPEL,
                INCREASE_ATTACK, POISON_CELL, SET_ON_FIRE, KILL_TARGETS, INDISARMABLE, UNPOISONABLE,
                HEAL, INVULNERABLE, HOLY_CELL, GIVE_FUNCTION));
    }
}
