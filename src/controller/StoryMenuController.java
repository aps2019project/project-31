package controller;

import constants.BattleManagerMode;
import constants.GameMode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.Account;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StoryMenuController implements Initializable {

    private static Scene scene;
    private static StoryMenuController storyMenu;
    public ImageView story1;
    public ImageView story2;
    public Button backButton;
    public ImageView story3;
    public StackPane mainContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(event -> PlayMenuController.getInstance().setAsScene());
        story1.setOnMouseClicked(event -> {
              BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 100,
                    100, GameMode.DeathMatch, 1);
            BattlePageController.getInstance().setAsScene();

        });
        story2.setOnMouseClicked(event -> {
            BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 100,
                    6, GameMode.Flag, 2);
            BattlePageController.getInstance().setAsScene();

        });
        story3.setOnMouseClicked(event -> {
               BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.Story, Account.getMainAccount(), 11,
                    100, GameMode.Domination, 3);
            BattlePageController.getInstance().setAsScene();

        });
    }

    public static StoryMenuController getInstance() {
        if (storyMenu == null) {
            storyMenu = new StoryMenuController();
        }
        return storyMenu;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/StoryPage.fxml"));
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

}
