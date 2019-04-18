package Model;

import java.util.ArrayList;

public class Spell extends Card {
    protected String target;

    public Spell(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account, String name,
                 int id, String type, boolean isDeployed, String target) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.target = target;
    }

    public void show() {
        System.out.println("Type: Spell - Name: "
                + this.getName()
                + " - MP : "
                + this.manaCost
                + " - Desc: "
                + this.cardText);

    }

    @Override
    public void showCardInfo() {

    }
}
