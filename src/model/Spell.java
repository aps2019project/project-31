package model;

import java.util.ArrayList;

public class Spell extends Card {


    public Spell(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account, String name,
                 int id, CardType type, boolean isDeployed) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
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
