package model;

import java.util.ArrayList;

public class Hero extends Deployable {
    protected int attack;
    protected int health;
    protected HeroSpell heroSpell;

    public Hero(int price, int manaCost, String cardText,
                ArrayList<Function> functions, Account account, String name,
                int id, CardType type, boolean isDeployed, boolean isMoved,
                boolean isAttacked, Cell cell, int currentHealth, int currentAttack,
                ArrayList<Buff> buffs, int attack, int health, HeroSpell heroSpell, int attackRange) {
        super(price, manaCost, cardText, functions, account, name, id, type,
                isDeployed, isMoved, isAttacked, cell, currentHealth, currentAttack, buffs, attackRange);
        this.attack = attack;
        this.health = health;
        this.heroSpell = heroSpell;
    }

    public void show() {
        System.out.println(" Name: "
                + getName()
                + " - AP : "
                + attack
                + " - HP : "
                + health
                + " class : "
                + attackType
                + " Special power : "
                + heroSpell.cardText + " Sell Cost : " + price);
    }

    @Override
    public void showCardInfo() {

    }


    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHeroSpell(HeroSpell heroSpell) {
        this.heroSpell = heroSpell;
    }


    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public HeroSpell getHeroSpell() {
        return heroSpell;
    }
}
