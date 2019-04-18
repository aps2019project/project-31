package model;

public class Buff {
    BuffType buffType;
    int turnsLeft;
    int powerBuffHealth;
    int powerBuffAttack;
    boolean isBeneficial;

    public Buff(BuffType buffType, int turnsLeft, int powerBuffHealth, int powerBuffAttack, boolean isBeneficial) {
        this.buffType = buffType;
        this.turnsLeft = turnsLeft;
        this.powerBuffHealth = powerBuffHealth;
        this.powerBuffAttack = powerBuffAttack;
        this.isBeneficial = isBeneficial;
    }

    public void setBuffType(BuffType buffType) {
        this.buffType = buffType;
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
    }

    public void setPowerBuffHealth(int powerBuffHealth) {
        this.powerBuffHealth = powerBuffHealth;
    }

    public void setPowerBuffAttack(int powerBuffAttack) {
        this.powerBuffAttack = powerBuffAttack;
    }

    public void setBeneficial(boolean beneficial) {
        isBeneficial = beneficial;
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    public int getPowerBuffHealth() {
        return powerBuffHealth;
    }

    public int getPowerBuffAttack() {
        return powerBuffAttack;
    }

    public boolean isBeneficial() {
        return isBeneficial;
    }

    public enum BuffType {
        Poison,
        Holy,
        Disarm,
        Weakness,
        Stun,
        Power
    }
}