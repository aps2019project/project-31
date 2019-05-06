package model;

import constants.AttackType;
import constants.CardType;

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

    public Minion duplicateDeployed(Cell cell,Account account) {
        return new Minion(price, manaCost, cardText, new ArrayList<Function>(functions), account, name,
                id, type, true, true, true, cell,
                attackRange, health, attack, BattleManager.generateUniqueId(this.id),
                attackType, isCombo, maxHealth, attack, health);
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
    public String toString() {
        return "Type : Minion - Name : "
                + this.name
                + " id : "
                + this.id
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

    public String infoToString() {
        try {
            if (cell != null)
                return "Minion: \nName: " + name + "\nDesc: " + cardText + "\nCoordination: " +
                        cell.getX1Coordinate() + " , " + cell.getX2Coordinate() + "\nunique card id: " + uniqueId + "\nHP: " +
                        currentHealth + "\nAP: " + currentAttack + "\nMP: " + manaCost + "\ntype: " + attackType + "\nRange: " + attackRange +
                        "\nCombo ability: " + isCombo + "\nStunned: " + isStunned();
            else
                return "Minion: \nName: " + name + "\nDesc: " + cardText + "\nCoordination: " + "\nHP: " +
                        maxHealth + "\nAP: " + attack + "\nMP: " + manaCost + "\ntype: " + attackType + "\nRange: " + attackRange +
                        "\nCombo ability: " + isCombo + "\nStunned: " + isStunned();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return " ";
    }

}
