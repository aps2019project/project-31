package model;

import java.util.ArrayList;

public class Spell extends Card {


    public Spell(int price, int manaCost, String cardText, ArrayList<Function> functions, Account account, String name,
                 int id, CardType type, boolean isDeployed) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
    }

    public String toString() {
        return "Type: Spell - Name: "
                + this.getName()
                + " - MP : "
                + this.manaCost
                + " - Desc: "
                + this.cardText;

    }

    public String infoToString() {
        return "Spell:\nName: " + name + "\nMP: " + manaCost + "\nDesc: " + cardText;
    }

    public void showCardInfo() {

    }
}
