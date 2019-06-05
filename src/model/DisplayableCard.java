package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DisplayableCard extends StackPane {
    private Card card;
    private String imagePath;
    private AnimatedGif mainIcon;


    public DisplayableCard(Card card, String imagePath) {
        this.setPrefSize(452, 592);
        this.setAlignment(Pos.CENTER);
        this.card = card;
        this.imagePath = imagePath;

        if (card instanceof Spell) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_spell.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            this.getChildren().add(imageView);
            addManaGem(card);
            if (imagePath.equals("")){
                imagePath = getClass().
                        getResource("/gifs/Spells/" + card.getName() +"/actionbar.gif")
                        .toExternalForm();
                System.out.println(imagePath);

                mainIcon = new AnimatedGif(imagePath,2);
                mainIcon.play();
            }

        }

        if (card instanceof Minion | card instanceof Hero) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_unit.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            this.getChildren().add(imageView);
            addManaGem(card);
            Label attack = new Label(((Deployable) card).getCurrentAttack() + "");
            Label health = new Label(((Deployable) card).getCurrentHealth() + "");

            attack.setFont(Font.font(17));
            health.setFont(Font.font(17));
            attack.setTextFill(Color.WHITE);
            health.setTextFill(Color.WHITE);
            getChildren().addAll(attack,health);
            attack.setTranslateX(-60);
            attack.setTranslateY(30);
            health.setTranslateY(30);
            health.setTranslateX(60);
        }

        if (card instanceof Item) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_artifact.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.resize(400, 600);
            this.getChildren().add(imageView);
        }

        Label cardName = new Label(card.getName());
        cardName.getStylesheets().add(getClass().getResource("/style/mainLabel.css").toExternalForm());
        cardName.setFont(Font.font(17));
        this.getChildren().add(cardName);
        String[] cardtext = card.getCardText().split(" ");
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < cardtext.length; i++) {
            text.append(cardtext[i] + " ");
            if (i % 4 == 3)
                text.append("\n");
        }
        Label cardText = new Label(text.toString());
        cardText.setFont(Font.font(12));
        cardText.setTextFill(Color.WHITE);
        cardText.setTranslateY(70);
        cardText.setAlignment(Pos.CENTER);
        getChildren().add(cardText);


    }

    private void addManaGem(Card card) {
        Image manaIcon = new Image(getClass().
                getResource("/assets/ui/icon_mana.png").toExternalForm());
        ImageView manaIconV = new ImageView(manaIcon);
        this.getChildren().add(manaIconV);
        manaIconV.setTranslateX(-100);
        manaIconV.setTranslateY(-120);
        Label manaCost = new Label("" + card.getManaCost());
        this.getChildren().add(manaCost);
        manaCost.setFont(Font.font(17));
        manaCost.setTranslateX(-100);
        manaCost.setTranslateY(-120);
    }
}

