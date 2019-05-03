package model;

import java.util.ArrayList;

public class Item extends Card {
    protected boolean isCollectible;

    public Item(int price, int manaCost, String cardText, ArrayList<Function> functions,
                Account account, String name, int id, CardType type, boolean isDeployed, boolean isCollectible) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isCollectible = isCollectible;
    }

    public boolean isCollectible() {
        return isCollectible;
    }


    @Override
    public String  toString() {
        return "Type: Item _ Name: "+ name+" _ MP: "+manaCost+" _ Desc: "+cardText;
    }

    @Override
    public void showCardInfo() {

    }
}
