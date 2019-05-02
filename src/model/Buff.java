package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Buff {
    BuffType buffType;
    private int turnsLeft;

    boolean isBeneficial;
    private boolean isContinuous;
    private boolean isActive;
    private int effectOfWeaknessOrPowerOnHealth = 0;
    private int effectOfWeaknessOrPowerOnAttack = 0;
    private int bleedOne = 0;
    private int bleedTwo = 0;


    public Buff(BuffType buffType, int turnsLeft, int effectOfWeaknessOrPowerOnHealth,
                int effectOfWeaknessOrPowerOnAttack, boolean isBeneficial) {
        this.buffType = buffType;
        this.turnsLeft = turnsLeft;
        this.effectOfWeaknessOrPowerOnAttack = effectOfWeaknessOrPowerOnAttack;
        this.effectOfWeaknessOrPowerOnHealth = effectOfWeaknessOrPowerOnHealth;
        this.isBeneficial = isBeneficial;
        this.isContinuous = false;
    }

    public void setBleed(int one, int two){
        bleedOne = one;
        bleedTwo = two;
    }


    public void setEffectOfWeaknessOrPowerOnHealth(int effectOfWeaknessOrPowerOnHealth) {
        this.effectOfWeaknessOrPowerOnHealth = effectOfWeaknessOrPowerOnHealth;
    }

    public void setEffectOfWeaknessOrPowerOnAttack(int effectOfWeaknessOrPowerOnAttack) {
        this.effectOfWeaknessOrPowerOnAttack = effectOfWeaknessOrPowerOnAttack;
    }



    public void setActive(boolean active) {
        isActive = active;
    }

    public void decreaseTurnsLeft() {
        turnsLeft--;
    }

    public int getEffectOfWeaknessOrPowerOnHealth() {
        return effectOfWeaknessOrPowerOnHealth;
    }

    public int getEffectOfWeaknessOrPowerOnAttack() {
        return effectOfWeaknessOrPowerOnAttack;
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public boolean isActive() {
        return isActive;
    }
    public void makeContinuous(){
        isContinuous = true;
    }

    public void setBuffType(BuffType buffType) {
        this.buffType = buffType;
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
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



    public boolean isBeneficial() {
        return isBeneficial;
    }

    public enum BuffType {
        Poison,
        Holy,
        Disarm,
        Weakness,
        Stun,
        Power,
        Unholy,
        Bleed
    }

    public static ArrayList<BuffType> getAllBuffs(){
        return new ArrayList<BuffType>(Arrays.asList(BuffType.Poison, BuffType.Holy, BuffType.Bleed,
                                                    BuffType.Weakness, BuffType.Stun, BuffType.Power,
                                                    BuffType.Unholy, BuffType.Disarm));
    }
}
