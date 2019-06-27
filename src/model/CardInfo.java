package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class CardInfo extends StackPane {
    private VBox status = new VBox();
    private Deployable deployable;

    public CardInfo(Deployable deployable) {
        this.setAlignment(Pos.CENTER);
        status.setPrefHeight(296);
        status.setPrefHeight(226);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().
                getResource("/assets/card_backgrounds/unusable_spell@2x.png").toExternalForm(),
                226, 296, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(backgroundImage));
        this.getChildren().add(status);
        this.deployable = deployable;
    }
}
