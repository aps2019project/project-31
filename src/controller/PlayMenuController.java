package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayMenuController implements Initializable {
    private static Scene scene;
    private static PlayMenuController playMenu;

    @FXML
    private StackPane mainContainer;
    @FXML
    private HBox mainHBox;
    @FXML
    private Button backButton;
    @FXML
    private ImageView customButton;
    @FXML
    private ImageView multiplayerButton;
    @FXML
    private ImageView storyButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double scaleX = screenWidth / mainContainer.getPrefWidth() * 2 / 3;
        mainContainer.setScaleX(scaleX);
        mainContainer.setScaleY(scaleX);
        backButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
        storyButton.setOnMouseClicked(event ->StoryMenuController.getInstance().setAsScene());
        customButton.setOnMouseClicked(event -> CustomGameController.getInstance().setAsScene());
        multiplayerButton.setOnMouseClicked(event -> WaitingPageController.getInstance().setAsScene());
    }

    public PlayMenuController() {
    }


    public static PlayMenuController getInstance() {
        if (playMenu == null) {
            playMenu = new PlayMenuController();
        }
        return playMenu;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/playMenu.fxml"));
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }
}
