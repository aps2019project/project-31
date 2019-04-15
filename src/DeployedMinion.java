import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class DeployedMinion extends Minion {
    private ArrayList<Condition> conditions;
    private Cell cell;
    private int currentHealth;
    private int curretAttack;
    private ArrayList<Buuf> buffs;

    public DeployedMinion(CardInfo cardInfo, int price, int manaCost, String cardText, ArrayList<Function> functions,
                          Account account, String functionTime, int attackRange, String attackType, int attack,
                          int health, ArrayList<Condition> conditions, Cell cell, int currentHealth,
                          int curretAttack, ArrayList<Buuf> buffs) {
        super(cardInfo, price, manaCost, cardText, functions, account, functionTime, attackRange, attackType, attack, health);
        this.conditions = conditions;
        this.cell = cell;
        this.currentHealth = currentHealth;
        this.curretAttack = curretAttack;
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

    public int getCurretAttack() {
        return curretAttack;
    }

    public ArrayList<Buuf> getBuffs() {
        return buffs;
    }
    public void show() {

    }
    public void move(){

    }
}
