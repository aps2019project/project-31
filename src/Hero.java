import java.util.ArrayList;

public class Hero extends Card {
    protected AttackType attackType;
    protected int attack;
    protected int health;
    protected HeroSpell heroSpell;

    public Hero(CardInfo cardInfo,
                int price,
                int manaCost,
                String cardText,
                ArrayList<Function> functions,
                Account account,
                AttackType attackType,
                int attack, int health,
                HeroSpell heroSpell) {
        super(cardInfo, price,
                manaCost, cardText,
                functions, account);
        this.attackType = attackType;
        this.attack = attack;
        this.health = health;
        this.heroSpell = heroSpell;
    }

    public void show() {
        System.out.println(" Name: "
                + getName()
                + " - AP : "
                + attack
                + " - HP : "
                + health
                + " class : "
                + attackType
                + " Special power : "
                + heroSpell.cardText + " Sell Cost : " + price);
    }

    @Override
    public void showCardInfo() {

    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHeroSpell(HeroSpell heroSpell) {
        this.heroSpell = heroSpell;
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

    public HeroSpell getHeroSpell() {
        return heroSpell;
    }
}
