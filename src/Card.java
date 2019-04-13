import java.util.ArrayList;

public class Card {
    private CardInfo cardInfo;
    private int price;
    private int manaCost;
    private String cardText;
    private ArrayList<Function> functions;
    private Account account;

    public void show(){


    }

    public boolean hasFunction(Function function){
        for (Function function1: functions){
            if (function.equals(function1)){
                return true;
            }
        }
        return false;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
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
}
