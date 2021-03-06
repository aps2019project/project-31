package model;

import constants.AttackType;
import constants.CardType;

import java.util.ArrayList;

public class Deployable extends Card {
    protected DisplayableDeployable face;
    protected boolean isMoved;
    protected boolean isAttacked;
    protected Cell cell;
    protected int attackRange;
    protected int currentHealth;
    protected int currentAttack;
    protected ArrayList<Buff> buffs = new ArrayList<>();
    protected int uniqueId;
    protected AttackType attackType;
    protected boolean isCombo;
    protected ArrayList<Pair<Deployable, Integer>> accumulatingAttacks = new ArrayList<>();
    protected int maxHealth;
    protected Item item;
    protected int damageTaken = 0;

    public Deployable(int price, int manaCost,
                      String cardText, ArrayList<Function> functions,
                      Account account, String name, int id, CardType type,
                      boolean isDeployed, boolean isMoved, boolean isAttacked, Cell cell,
                      int attackRange, int currentHealth, int currentAttack, int uniqueId,
                      AttackType attackType, boolean isCombo, int maxHealth) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isMoved = isMoved;
        this.isAttacked = isAttacked;
        this.cell = cell;
        this.attackRange = attackRange;
        this.currentHealth = currentHealth;
        this.currentAttack = currentAttack;
        this.uniqueId = uniqueId;
        this.attackType = attackType;
        this.isCombo = isCombo;
        this.maxHealth = maxHealth;
        this.hasFlag = false;
    }

    public DisplayableDeployable getFace() {
        return face;
    }

    public void setFace(DisplayableDeployable face) {
        this.face = face;
    }

    public Item getItem() {
        return item;
    }

    protected boolean hasFlag;

    public boolean doesHaveFlag() {
        return hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public boolean isCombo() {
        return isCombo;
    }

    public void addFunction(Function function) {
        functions.add(function);
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
        damageTaken -= amount;
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

    public void setItem(Item item) {
        this.item = item;
    }


    public void setBuffs(ArrayList<Buff> buffs) {
        this.buffs = buffs;
    }

    public void setAttackType(AttackType attackType) {
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

    public AttackType getAttackType() {
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
        return currentHealth + extraHealthDueToBuff - damageTaken;
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
        damageTaken += damage;
    }

    @Override
    public String toString() {
        if (this.type == CardType.hero)
            return this.toString();
        if (this.type == CardType.minion)
            return this.toString();
        return " ";
    }


    public void applyFire() {
        currentHealth--; // how much it decreases
    }

    public void applyPoison() {
        currentHealth--; //whats the difference between this and fire
    }

    public String infoToString() {
        return " ";
    }

    public String shortVersionString() {
        return "Name: " + name + "  attackType: " + attackType +
                "  Coordination: " + cell.getX1Coordinate() + " , " + cell.getX2Coordinate() +
                "  unique card id: " + uniqueId + "  HP: " + theActualHealth() + "  AP: " + theActualDamage();
    }


    public String shortestVersionString() {
        return "Name: " + name + "  attackType: " + attackType;
    }

    public boolean doesBelongToPlayer(Player player) {
        for (Deployable deployable : player.cardsOnBattleField) {
            if (deployable == this)
                return true;
        }
        return false;
    }

}
