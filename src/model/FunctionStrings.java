package model;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionStrings {
    protected static final String STUN = "stun target for";
    protected static final String ACCUMULATING_ATTACKS = "accumulate previous attacks on target";
    protected static final String DISARM = "disarm target for";
    protected static final String APPLY_POISON = "poison target for";
    protected static final String DEAL_DAMAGE = "deal damage";
    protected static final String IGNORE_HOLYBUFF = "ignore holybuff";
    protected static final String APPLY_CONDITION = "apply condition";
    protected static final String APPLY_BUFF = "apply buff";
    protected static final String REVERT_DISARM = "revert disarm";
    protected static final String REMOVE_POISON = "remove poison";
    protected static final String REMOVE_DEBUFFS = "remove debuffs";
    protected static final String IGNORE_ATTACK = "ignore attack";
    protected static final String IGNORE_LESSER_ATTACK = "ignore lesser attacks";
    protected static final String REMOVE_BENEFICIALS = "remove beneficial effects";
    protected static final String DISPEL = "dispels target";
    protected static final String INCREASE_ATTACK = "increase attack";
    protected static final String POISON_CELL = "poison target cells";
    protected static final String SET_ON_FIRE = "set target cell on fire";
    protected static final String KILL_TARGETS = "straight up murder fools";
    protected static final String INDISARMABLE = "indisarmable";
    protected static final String UNPOISONABLE = "unpoisonable";
    protected static final String BLEED = "bleed";
    protected static final String HEAL = "heal";
    protected static final String INVULNERABLE = "invulnerable";
    protected static final String HOLY_CELL = "holy cell";
    protected static final String GIVE_FUNCTION = "give function";

    public static ArrayList<String> allFunctionStrings(){
       return new ArrayList<>(Arrays.asList(STUN, ACCUMULATING_ATTACKS, DEAL_DAMAGE,
               IGNORE_HOLYBUFF, APPLY_BUFF, IGNORE_LESSER_ATTACK, DISPEL,
               INCREASE_ATTACK, POISON_CELL, SET_ON_FIRE, KILL_TARGETS, INDISARMABLE, UNPOISONABLE,
               BLEED, HEAL, INVULNERABLE, HOLY_CELL, GIVE_FUNCTION));
    }
}
