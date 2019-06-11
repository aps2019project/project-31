package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(event -> PlayMenuController.getInstance().setAsScene());

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
