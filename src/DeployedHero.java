import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class DeployedHero extends Hero {
    private ArrayList<Condition> conditions;
    private Cell cell;
    private int currentHealth;
    private int currentAttack;
    private ArrayList<Buff> buffs;

    public DeployedHero(
            CardInfo cardInfo, int price, int manaCost, String cardText, ArrayList<Function> functions, Account account,
            AttackType attackType, int attack, int health, HeroSpell heroSpell, ArrayList<Condition> conditions
            , Cell cell, int currentHealth, int currentAttack, ArrayList<Buff> buffs) {
        super(cardInfo, price, manaCost, cardText, functions, account, attackType, attack, health, heroSpell);
        this.conditions = conditions;
        this.cell = cell;
        this.currentHealth = currentHealth;
        this.currentAttack = currentAttack;
        this.buffs = buffs;
    }

    public ArrayList<Condition> getConditions() {
        return conditions;
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

    public ArrayList<Buff> getBuffs() {
        return buffs;
    }

    public void show() {

    }
    public void move(){

    }
}
