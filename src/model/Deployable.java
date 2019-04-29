package model;

import java.util.ArrayList;

public class Deployable extends Card {
    protected boolean isMoved;
    protected boolean isAttacked;
    protected Cell cell;
    protected int currentHealth;
    protected int currentAttack;
    protected int attackRange;
    protected ArrayList<Buff> buffs;
    protected int uniqueId;
    protected String attackType;
    protected boolean isCombo;

    public int getUniqueId() {
        return uniqueId;
    }

    public boolean isCombo() {
        return isCombo;
    }

    public void addFunction(Function function){
        functions.add(function);
    }

    protected ArrayList<Pair<Deployable, Integer>> accumulatingAttacks;
    protected int maxHealth;

    public Deployable(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account,
                      String name, int id, CardType type, boolean isDeployed, boolean isMoved, boolean isAttacked,
                      Cell cell, int currentHealth, int currentAttack,
                      ArrayList<Buff> buffs) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isMoved = isMoved;
        this.isAttacked = isAttacked;
        this.cell = cell;
        this.maxHealth = this.currentHealth = currentHealth;
        this.currentAttack = currentAttack;
        this.buffs = buffs;
    }

    public int accumulatingAttack(Deployable attacker) {
        for (Pair<Deployable, Integer> pair : accumulatingAttacks) {
            if (pair.getFirst().equals(attacker)) {
                pair.setSecond(pair.getSecond().intValue() + 1);
                return pair.getSecond() - 1;
            }
        }
        accumulatingAttacks.add(new Pair<>(attacker, 1));
        return 0;
    }

    public void healUp(int amount) {
        currentHealth += amount;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void increaseAttack(int amount) {
        currentAttack += amount;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public void setAttacked(boolean attacked) {
        isAttacked = attacked;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setBuffs(ArrayList<Buff> buffs) {
        this.buffs = buffs;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public boolean isAttacked() {
        return isAttacked;
    }

    public Cell getCell() {
        return cell;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public ArrayList<Buff> getBuffs() {
        return buffs;
    }

    public String getAttackType() {
        return attackType;
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public boolean isStunned() {
        for (Buff buff : buffs) {
            if (buff.buffType == Buff.BuffType.Stun && buff.isActive() && buff.getTurnsLeft() > 0)
                return true;
        }
        return false;
    }

    public int theActualDamage() {
        int extraDamageDueToBuff = 0;
        for (Buff buff : buffs) {
            if (buff.buffType == Buff.BuffType.Power || buff.buffType == Buff.BuffType.Weakness) {
                if (buff.getTurnsLeft() > 0 && buff.isActive())
                    extraDamageDueToBuff += buff.getEffectOfWeaknessOrPowerOnAttack();
            }
        }
        return currentAttack + extraDamageDueToBuff;
    }

    public boolean isDisarmed() {
        for (Buff buff : buffs) {
            if (buff.buffType == Buff.BuffType.Disarm && buff.isActive() && buff.getTurnsLeft() > 0)
                return true;
        }
        return false;
    }

    public int theActualHealth() {
        int extraHealthDueToBuff = 0;
        for (Buff buff : buffs) {
            if (buff.buffType == Buff.BuffType.Power || buff.buffType == Buff.BuffType.Weakness) {
                if (buff.getTurnsLeft() > 0 && buff.isActive())
                    extraHealthDueToBuff += buff.getEffectOfWeaknessOrPowerOnHealth();
            }
        }
        return currentHealth + extraHealthDueToBuff;
    }

    public int theActualDamageReceived(int theDamage) {
        int howManyHolyBuffs = 0;
        for (Buff buff : buffs) {
            if (buff.buffType == Buff.BuffType.Holy && buff.isActive() && buff.getTurnsLeft() > 0)
                howManyHolyBuffs++;
        }
        return theDamage - howManyHolyBuffs;
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    @Override
    public void show() {

    }

    @Override
    public void showCardInfo() {

    }

    public void applyFire() {
        currentHealth--; // how much it decreases
    }

    public void applyPoison() {
        currentHealth--; //whats the difference between this and fire
    }
}
