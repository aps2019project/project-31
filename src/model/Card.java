package model;

import constants.CardType;
import view.Output;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class Card {
    protected int price;
    protected int manaCost;
    protected String cardText;
    protected ArrayList<Function> functions;
    protected Account account;
    protected String name;
    protected int id;
    protected CardType type;
    protected boolean isDeployed = false;


    public Card(int price, int manaCost, String cardText,
                ArrayList<Function> functions, Account account,
                String name, int id, CardType type, boolean isDeployed) {
        this.price = price;
        this.manaCost = manaCost;
        this.cardText = cardText;
        this.functions = functions;
        this.account = account;
        this.name = name;
        this.id = id;
        this.type = type;
        this.isDeployed = isDeployed;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    abstract public String toString();

    public void show() {
        Output.print(this.toString());
    }


    public boolean hasFunction(Function function) {
        for (Function function1 : functions) {
            if (function.equals(function1)) {
                return true;
            }
        }
        return false;
    }

    public int getPrice() {
        return price;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getCardText() {
        return cardText;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isDeployed() {
        return isDeployed;
    }

    public boolean equals(Object o) {
        return equals((Card) o);
    }

    private boolean equals(Card card) {
        if(this ==null || card ==null)
            return false;
        return card.name.equals(this.name) ||
                card.id == this.id;
    }

    public int getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }
    public String infoToString() {
        return " ";
    }

}
