import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class DeployedMinion extends Minion implements MoveableDeployed{
    private ArrayList<Condition> conditions;
    private Cell cell;
    private int currentHealth;
    private int currentAttack;
    private ArrayList<Buff> buffs;

    public DeployedMinion(int price, int manaCost, String cardText,
                          ArrayList<Function> functions,
                          Account account, String name, int id,
                          String type, boolean isDeployed,
                          String functionTime, int attackRange,
                          String attackType, int attack, int health,
                          Cell cell, int currentHealth,
                          int currentAttack) {
        super(price, manaCost, cardText, functions,
                account, name, id, type,
                isDeployed, functionTime,
                attackRange, attackType, attack,
                health);
        this.conditions = null;
        this.cell = cell;
        this.currentHealth = currentHealth;
        this.currentAttack = currentAttack;
        this.buffs = null;
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
        if(Map.getDistance(this.cell,cell)<=Map.getMaxMoveRange()){

        }
    }

    @Override
    public void attack(Cell cell) {
        if(Map.getDistance(cell,this.cell)<=Map.getMaxMoveRange()){

        }
    }
}
