package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class Deployable extends Card {
    protected boolean isMoved;
    protected boolean isAttacked;
    protected Cell cell;
    protected int currentHealth;
    protected int currentAttack;
    protected int attackRange;
    protected ArrayList<Buff> buffs;

    public Deployable(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account,
                      String name, int id, CardType type, boolean isDeployed, boolean isMoved, boolean isAttacked,
                      Cell cell, int currentHealth, int currentAttack,
                      ArrayList<Buff> buffs) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isMoved = isMoved;
        this.isAttacked = isAttacked;
        this.cell = cell;
        this.currentHealth = currentHealth;
        this.currentAttack = currentAttack;
        this.buffs = buffs;
    }

    public void addBuff(Buff buff){
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
            if (buff.buffType == Buff.BuffType.Disarm && buff.isActive() && buff.turnsLeft > 0)
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

    @Override
    public void show() {

    }

    @Override
    public void showCardInfo() {

    }
}
