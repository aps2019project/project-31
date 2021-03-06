package model;

import constants.CardType;

import java.util.ArrayList;

public class HeroSpell extends Spell {
    private final int cooldown;
    private int coolDownRemaining;

    public HeroSpell(int price, int manaCost, String cardText,
                     ArrayList<Function> functions, Account account,
                     String name, int id, CardType type, boolean isDeployed,
                     int cooldown, int coolDownRemaining) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.cooldown = cooldown;
        this.coolDownRemaining = coolDownRemaining;
    }

    public void goOnCooldown() {
        coolDownRemaining = cooldown;
    }

    public void reduceCooldownRemaining(int num) {
        coolDownRemaining -= num;
        if (coolDownRemaining < 0)
            coolDownRemaining = 0;
    }

    public void decrementCooldonwRemaining() {
        coolDownRemaining--;
        if (coolDownRemaining < 0)
            coolDownRemaining = 0;
    }

    public void setCoolDownRemaining(int coolDownRemaining) {
        this.coolDownRemaining = coolDownRemaining;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCoolDownRemaining() {
        return coolDownRemaining;
    }

}
