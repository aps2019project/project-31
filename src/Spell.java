import java.util.ArrayList;

public class Spell extends Card {
    protected String target;

    public Spell(CardInfo cardInfo,
                 int price,
                 int manaCost,
                 String cardText,
                 ArrayList<Function> functions,
                 Account account,
                 String target) {
        super(cardInfo, price,
                manaCost, cardText,
                functions, account);
        this.target = target;
    }

    public void show() {
        System.out.println("Type: Spell - Name: "
                + this.getName()
                + " - MP : "
                + this.manaCost
                + " - Desc: "
                + this.cardText);

    }
}
