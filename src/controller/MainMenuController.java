package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.Account;
import model.Client;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    private static Scene scene;
    private static MainMenuController mainMenu;
    @FXML
    public StackPane mainContainer;
    public ImageView backgroundImage;
    public Button playButton;
    public Button shopButton;
    public Button leaderboardButton;
    public Button collectionButton;
    public BorderPane borderPane;
    public Button logoutButton;
    public Button saveButton;
    public Button cardMakerButton;
    public Button matchHistories;
    public Button chatRoom;


    public MainMenuController() {
    }


    public static MainMenuController getInstance() {
        if (mainMenu == null) {
            mainMenu = new MainMenuController();
        }
        return mainMenu;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/mainMenu.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoutButton.setOnAction(event -> {
            try {
                Client.getClient().logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LoginPageController.getInstance().setAsScene();
            Account.setMainAccount(null);
        });
        leaderboardButton.setOnAction(event -> {
            LeaderBoardController.getInstance().updateLeaderBoard();
            LeaderBoardController.getInstance().setAsScene();
        });
        matchHistories.setOnAction(event -> {
            MatchHistoryController.getInstance().setAsScene();
        });
        /*chatRoom.setOnAction(event -> {

        });*/
        playButton.setOnAction(event -> PlayMenuController.getInstance().setAsScene());
        shopButton.setOnAction(event -> ShopController.getInstance().setAsScene());
        collectionButton.setOnAction(event -> CollectionController.getInstance().setAsScene());
        saveButton.setOnAction(event -> Account.saveAllAccounts());
        cardMakerButton.setOnAction(event -> CardMakerController.getInstance().setAsScene());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Double screenWidth = screen.getWidth();
        backgroundImage.setScaleX(screenWidth / backgroundImage.getFitWidth() * 2 / 3);
        backgroundImage.setScaleY(screenWidth / backgroundImage.getFitWidth() * 2 / 3);
    }
}
