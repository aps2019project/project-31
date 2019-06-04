package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class DisplayableCard extends StackPane {
    private Card card;
    private String imagePath;


    public DisplayableCard(Card card, String imagePath) {
        this.setPrefSize(452,592);
        this.setAlignment(Pos.TOP_LEFT);
        this.card = card;
        this.imagePath = imagePath;
        if (card instanceof Spell) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_spell.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            this.getChildren().add(imageView);
            addManaGem(card);
        }

        if (card instanceof Minion | card instanceof Hero) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_unit.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            this.getChildren().add(imageView);
            addManaGem(card);
        }

        if (card instanceof Item) {
            Image image = new Image(getClass().
                    getResource("/assets/card_backgrounds/neutral_prismatic_artifact.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.resize(400, 600);
            this.getChildren().add(imageView);
        }




    }

    private void addManaGem(Card card) {
        Image manaIcon = new Image(getClass().
                getResource("/assets/ui/icon_mana.png").toExternalForm());
        ImageView manaIconV = new ImageView(manaIcon);
        this.getChildren().add(manaIconV);
        manaIconV.setTranslateX(-10);
        manaIconV.setTranslateY(-10);
        Label manaCost = new Label("" + card.getManaCost());
        this.getChildren().add(manaCost);
        manaCost.setFont(Font.font(17));
        manaCost.setTranslateX(12);
        manaCost.setTranslateY(10);
    }
}

