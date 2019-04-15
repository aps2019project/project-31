import java.util.ArrayList;

public class Minion extends Card {
    public Minion(CardInfo cardInfo, int price, int manaCost, String cardText, ArrayList<Function> functions,
                  Account account) {
        super(cardInfo, price, manaCost, cardText, functions, account);
    }

    @Override
    public void show() {

    }

    @Override
    public void showCardInfo() {

    }
}
