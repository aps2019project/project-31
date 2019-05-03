package model;

import constants.AttackType;

import java.util.ArrayList;

public class Minion extends Deployable {

    private int attack;
    private int health;

    public Minion(int price, int manaCost, String cardText,
                  ArrayList<Function> functions, Account account, String name,
                  int id, CardType type, boolean isDeployed, boolean isMoved,
                  boolean isAttacked, Cell cell, int attackRange, int currentHealth,
                  int currentAttack, int uniqueId, AttackType attackType, boolean isCombo,
                  int maxHealth, int attack, int health) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed,
                isMoved, isAttacked, cell, attackRange, currentHealth, currentAttack,
                uniqueId, attackType, isCombo, maxHealth);
        this.attack = attack;
        this.health = health;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String  toString() {
        return "Type : Minion - Name : "
        + this.name
        + " - Class : "
        + this.attackType
        + " - AP : "
        + this.attack
        + " - HP : "
        + this.health
        + " - MP : "
        + this.manaCost
        + " - Special power :"
        + this.cardText;
    }

    @Override
    public void showCardInfo() {

    }
}
