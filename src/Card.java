import java.util.ArrayList;

abstract public class Card {
    protected CardInfo cardInfo;
    protected int price;
    protected int manaCost;
    protected String cardText;
    protected ArrayList<Function> functions;
    protected Account account;

    public Card(CardInfo cardInfo,
                int price,
                int manaCost,
                String cardText,
                ArrayList<Function> functions,
                Account account) {
        this.cardInfo = cardInfo;
        this.price = price;
        this.manaCost = manaCost;
        this.cardText = cardText;
        this.functions = functions;
        this.account = account;
    }

    public String getName() {
        return cardInfo.getName();
    }

    abstract public void show();
    abstract public void showCardInfo(); // farq mikone :|||
    public boolean hasFunction(Function function) {
        for (Function function1 : functions) {
            if (function.equals(function1)) {
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
