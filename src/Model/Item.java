package Model;

import java.util.ArrayList;

public class Item extends Card {
    protected boolean isCollectable;

    public Item(int price, int manaCost, String cardText, ArrayList<Function> functions,
                Account account, String name, int id, CardType type, boolean isDeployed, boolean isCollectable) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isCollectable = isCollectable;
    }

    public boolean isCollectable() {
        return isCollectable;
    }


    @Override
    public void show() {

    }

    @Override
    public void showCardInfo() {

    }
}
