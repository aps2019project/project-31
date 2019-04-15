import java.util.ArrayList;

public class DeployedMinion extends Minion {
    public DeployedMinion(CardInfo cardInfo, int price, int manaCost, String cardText,
                          ArrayList<Function> functions, Account account) {
        super(cardInfo, price, manaCost, cardText, functions, account);
    }
}
