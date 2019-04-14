import java.util.ArrayList;

public class HeroSpell extends Spell{
    private int cooldown;
    private int coolDownRemaining;

    public HeroSpell(CardInfo cardInfo,
                     int price,
                     int manaCost,
                     String cardText,
                     ArrayList<Function> functions,
                     Account account,
                     String target,
                     int cooldown) {
        super(cardInfo, price, manaCost, cardText, functions, account, target);
        this.cooldown = cooldown;
        this.coolDownRemaining = 0;
    }

    public void goOnCooldown(){
        coolDownRemaining = cooldown;
    }

    public void reduceCooldownRemaining(int num){
        coolDownRemaining -= num;
        if (coolDownRemaining < 0)
            coolDownRemaining = 0;
    }

    public void decrementCooldonwRemaining(){
        coolDownRemaining--;
        if (coolDownRemaining < 0)
            coolDownRemaining = 0;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
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
