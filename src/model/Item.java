package model;

import constants.CardType;

import java.util.ArrayList;

public class Item extends Card {
    protected boolean isCollectible;
    protected int uniqueId;

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Item(int price, int manaCost, String cardText, ArrayList<Function> functions,
                Account account, String name, int id, CardType type, boolean isDeployed, boolean isCollectible) {
        super(price, manaCost, cardText, functions, account, name, id, type, isDeployed);
        this.isCollectible = isCollectible;
    }


    public boolean isCollectible() {
        return isCollectible;
    }


    @Override
    public String toString() {
        return "Type: Item _ Name: "
                + name
                + " _ MP: "
                + manaCost
                + " _ Desc: "
                + cardText;
    }

    public String infoToString() {
        try {
            return "Item  \nName:" + name + "\nDesc: " + cardText + "\nCoordination: " +
                    findCellByCollectibleItem().getX1Coordinate() + " , " +
                    findCellByCollectibleItem().getX2Coordinate();
        }catch (Exception e){
            System.err.println("Item not in map!");
        }
        return " ";

    }

    public Cell findCellByCollectibleItem() {
        for (Cell[] cells : Map.getMap()) {
            for (Cell cell : cells) {
                if (cell.getItem().getUniqueId() == uniqueId)
                    return cell;
            }
        }
        return null;
    }
}
