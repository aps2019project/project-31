import java.util.ArrayList;

public class Minion extends Card {
    protected String functionTime;
    protected int attackRange;
    protected String attackType;
    private int attack;
    private int health;

    public Minion(CardInfo cardInfo, int price, int manaCost, String cardText, ArrayList<Function> functions,
                  Account account, String functionTime, int attackRange, String attackType, int attack, int health) {
        super(cardInfo, price, manaCost, cardText, functions, account);
        this.functionTime = functionTime;
        this.attackRange = attackRange;
        this.attackType = attackType;
        this.attack = attack;
        this.health = health;
    }

    public String getFunctionTime() {
        return functionTime;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public String getAttackType() {
        return attackType;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void show() {

    }

    @Override
    public void showCardInfo() {

    }
}
