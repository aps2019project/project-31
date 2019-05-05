package model;

import constants.AttackType;
import constants.CardType;

import java.util.ArrayList;

public class Hero extends Deployable {
    protected int attack;
    protected int health;
    protected HeroSpell heroSpell;

    public Hero(int price, int manaCost,
                String cardText, ArrayList<Function> functions,
                Account account, String name, int id, CardType type,
                boolean isDeployed, boolean isMoved, boolean isAttacked,
                Cell cell, int attackRange, int currentHealth, int currentAttack,
                int uniqueId, AttackType attackType, boolean isCombo,
                int maxHealth, int attack, int health, HeroSpell heroSpell) {
        super(price, manaCost, cardText, functions,
                account, name, id, type, isDeployed, isMoved,
                isAttacked, cell, attackRange, currentHealth,
                currentAttack, uniqueId, attackType, isCombo, maxHealth);
        this.attack = attack;
        this.health = health;
        this.heroSpell = heroSpell;
    }

    public Hero duplicateHero() {
        return new Hero(this.price, this.manaCost, this.cardText, this.functions, account, name, id, type, isDeployed,
                true, true, cell, attackRange, currentHealth, currentAttack, id, attackType,
                isCombo, maxHealth, attack, health, heroSpell);
    }

    public String toString() {
        return " Name: "
                + getName()
                + " - AP : "
                + attack
                + " - HP : "
                + health
                + " class : "
                + attackType
                + " Special power : "
                + heroSpell.cardText + " Sell Cost : " + price;
    }

    public String infoToString() {
        if (cell != null)
            return "Hero: \nName: " + name + "\nCost: " + heroSpell.manaCost + "\nDesc: " + cardText +
                    "\nCoordination: " + cell.getX1Coordinate() + " , " + cell.getX2Coordinate() +
                    "\nunique card id: " + uniqueId + "\nHP: " + currentHealth + "\nAP: " + currentAttack;
        else
            return "Hero: \nName: " + name + "\nCost: " + heroSpell.manaCost + "\nDesc: " + cardText +
                    "\nHP: " + currentHealth + "\nAP: " + currentAttack;
    }

//    public Hero deepClone( ){
//
//    }

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
