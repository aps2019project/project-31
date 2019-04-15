import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class DeployedHero {
    private ArrayList<Condition> conditions;
    private Cell cell;
    private int currentHealth;
    private int currentAttack;
    private ArrayList<Buff> buffs;

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
    public void show(){

    }
}
