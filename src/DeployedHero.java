import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class DeployedHero extends Hero implements MoveableDeployed{
    private ArrayList<Condition> conditions;
    private Cell cell;
    private int currentHealth;
    private int currentAttack;
    private ArrayList<Buff> buffs;

    public DeployedHero(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account,
                        String name, int id, String type, boolean isDeployed, AttackType attackType, int attack,
                        int health, HeroSpell heroSpell, ArrayList<Condition> conditions, Cell cell, int currentHealth,
                        int currentAttack, ArrayList<Buff> buffs) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed, attackType, attack,
                health, heroSpell);
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

    @Override
    public void move(Cell cell) {

    }

    @Override
    public void attack(Cell cell) {

    }
}
