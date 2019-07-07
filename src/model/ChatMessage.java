package model;

import controller.LoginPageController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


public class ChatMessage extends StackPane {
    private ImageView background;
    private Label text;

    public ChatMessage(String text, boolean isNative) {
        if (isNative) {
            this.text = makeMainLabel(text, 17);
            background = new ImageView(new Image(getClass()
                    .getResource("/assets/ui/notification_go.png").toExternalForm()));
            this.getChildren().addAll(background, this.text);

        }else {
            this.text = LoginPageController.getInstance().makeMainLabel(text, 17);
            background = new ImageView(new Image(getClass()
                    .getResource("/assets/ui/notification_enemy_turn.png").toExternalForm()));
            this.getChildren().addAll(background, this.text);
        }
        background.setFitHeight(50);
        background.setFitWidth(700);
    }

    public Label makeMainLabel(String text, int fontsize) {
        Label label = new Label(text);
        label.getStylesheets().add(getClass().getResource("/style/secondaryLabel.css").toExternalForm());
        label.setFont(Font.font(fontsize));
        label.setAlignment(Pos.CENTER);
        return label;
    }

}
