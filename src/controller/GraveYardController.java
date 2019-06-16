package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraveYardController implements Initializable {
    private static Scene scene;
    private static GraveYardController graveYard;

    public Button backToBattle;
    public GridPane gridPanePlayer2;
    public ImageView player2Profile;
    public ImageView player1Profile;
    public Label username;
    public Label opponentUsername;
    public GridPane gridPanePlayer1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* username.setText(BattlePageController.getInstance().getMe().getAccount().getUsername());
        opponentUsername.setText(BattlePageController.getInstance().getOpponent().getAccount().getUsername());*/
        backToBattle.setOnAction(event -> {
            BattlePageController.getInstance().setAsScene();
        });

    }
    public static GraveYardController getInstance() {
        if (graveYard == null) {
            graveYard = new GraveYardController();
        }
        return graveYard;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/graveYard.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }
}
