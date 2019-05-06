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


    public Hero duplicateDeployed(BattleManager battleManager, Account account) {
        Cell cell;
        if (this.account.equals(battleManager.getPlayer1())) {
            cell = Map.getCell(3, 1);
        } else {
            cell = Map.getCell(3, 9);
        }
        return new Hero(this.price, this.manaCost, this.cardText, this.functions, account, name, id, type, true,
                true, true, cell, attackRange, currentHealth, currentAttack, id, attackType,
                isCombo, maxHealth, attack, health, heroSpell);
    }

    public Hero duplicateDeployed(Account account, int howFucked) {
        Cell cell;
        if (howFucked % 2 == 0) {
            cell = Map.getCell(3, 1);
        } else {
            cell = Map.getCell(3, 9);
        }
        return new Hero(this.price, this.manaCost, this.cardText, this.functions,
                account, name, id, type, true,
                true, true, cell, attackRange, currentHealth,
                currentAttack, id * 100 + howFucked % 2,
                attackType, isCombo, maxHealth, attack, health, heroSpell);
    }

    @Override
    public String toString() {
        return " Name : "
                + this.name
                + " id : "
                + this.id
                + " - AP : "
                + theActualDamage()
                + " - HP : "
                + theActualHealth()
                + " class : "
                + attackType
                + " Special power : "
                + heroSpell.cardText + " Sell Cost : " + price;
    }

    public String infoToString() {
        if (cell != null)
            return "Hero: \nName: " + name + "\nCost: " + heroSpell.manaCost + "\nDesc: " + cardText +
                    "\nCoordination: " + cell.getX1Coordinate() + " , " + cell.getX2Coordinate() +
                    "\nunique card id: " + uniqueId + "\nHP: " + theActualHealth() + "\nAP: " + theActualDamage();
        else
            return "Hero: \nName: " + name + "\nCost: " + heroSpell.manaCost + "\nDesc: " + cardText +
                    "\nHP: " + theActualHealth() + "\nAP: " + theActualDamage();
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
