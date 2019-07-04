package controller;

import constants.GameMode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Client;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingPageController implements Initializable {
    private static Scene scene;
    public Button cancel;
    public HBox mainHBox;
    public StackPane mode1;
    public ImageView deathMatchBtn;
    public StackPane mode2;

    public StackPane mode3;
    public ImageView dominationBtn;
    public Label status;
    private static WaitingPageController waitingPage;
    public ImageView flagBtn;

    public static WaitingPageController getInstance() {
        if (waitingPage == null) {
            waitingPage = new WaitingPageController();
        }
        return waitingPage;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/WaitingPage.fxml"));
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        waitingPage = this;
        status.setText("Click One Mode.");
        deathMatchBtn.setOnMouseClicked(event -> {
            removeModesFromPage();
            Client.getClient().sendPlayRequest(GameMode.DeathMatch);

        });
        flagBtn.setOnMouseClicked(event -> {
            removeModesFromPage();
            Client.getClient().sendPlayRequest(GameMode.Flag);

        });
        dominationBtn.setOnMouseClicked(event -> {
            removeModesFromPage();
            Client.getClient().sendPlayRequest(GameMode.Domination);

        });

        cancel.setOnMouseClicked(mouseEvent -> {

        });
    }

    private void removeModesFromPage(){
        mainHBox.getChildren().remove(mode1);
        mainHBox.getChildren().remove(mode2);
        mainHBox.getChildren().remove(mode3);
        status.setText("waiting ...");
    }


}
