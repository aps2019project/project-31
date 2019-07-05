package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import model.DisplayableDeployable;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraveYardController implements Initializable {
    private static Scene scene;
    private static GraveYardController graveYard;

    public Button backToBattle;
    public ImageView player2Profile;
    public ImageView player1Profile;
    public Label username;
    public Label opponentUsername;
    public ListView<DisplayableDeployable> player2List;
    public ListView<DisplayableDeployable> player1List;

    public static GraveYardController getInstance() {
        if (graveYard == null)
            return graveYard = new GraveYardController();
        return graveYard;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graveYard = this;
        username.setText(SinglePlayerBattlePageController.getInstance().getMe().getAccount().getUsername());
        opponentUsername.setText(SinglePlayerBattlePageController.getInstance().getOpponent().getAccount().getUsername());
        backToBattle.setOnAction(event -> {

            SinglePlayerBattlePageController.getInstance().setAsScene();

        });
        player1Profile = SinglePlayerBattlePageController.getInstance().player1Profile;
        player2Profile = SinglePlayerBattlePageController.getInstance().player2Profile;
    }

    public void updateGraveYard() {
        GraveYardController.getInstance().player1List.getItems().clear();
        GraveYardController.getInstance().player2List.getItems().clear();
        System.err.println(BattleMenu.getBattleManager().getPlayer1().getGraveYard().size() + " " + BattleMenu.getBattleManager().getPlayer2().getGraveYard().size());
        GraveYardController.getInstance().player1List.getItems().addAll(BattleMenu.getBattleManager().getPlayer1().getGraveYard());
        GraveYardController.getInstance().player2List.getItems().addAll(BattleMenu.getBattleManager().getPlayer2().getGraveYard());
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
        updateGraveYard();
        Initializer.setCurrentScene(scene);
//        gridPanePlayer1.getChildren().removeAll(gridPanePlayer1.getChildren());
//        gridPanePlayer2.getChildren().removeAll(gridPanePlayer2.getChildren());
//        gridPanePlayer1.getChildren().addAll(BattleMenu.getBattleManager().getPlayer1().getGraveYard());
//        gridPanePlayer2.getChildren().addAll(BattleMenu.getBattleManager().getPlayer2().getGraveYard());
    }
}
