package model;

import java.util.ArrayList;

public class Minion extends Deployable {
    protected String functionTime;

    private int attack;
    private int health;

    public Minion(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account, String name,
                  int id, CardType type, boolean isDeployed, boolean isMoved, boolean isAttacked, Cell cell, int currentHealth, int currentAttack,
                  ArrayList<Buff> buffs, String functionTime, int attackRange, String attackType,
                  int attack, int health) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed, isMoved, isAttacked, cell, currentHealth, currentAttack, buffs);
        this.functionTime = functionTime;
        this.attackRange = attackRange;
        this.attackType = attackType;
        this.attack = attack;
        this.health = health;
    }

    public String getFunctionTime() {
        return functionTime;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public String getAttackType() {
        return attackType;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void show() {
        System.out.println("Type : Minion - Name : "
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
        + this.cardText);
    }

    @Override
    public void showCardInfo() {

    }
}
