package Server;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerPageController implements Initializable {
    private static ServerPageController instance = null;
    public Button dataButton;

    private static Scene scene;
    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ServerPageController.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ServerInitializer.setCurrentScene(scene);
    }

    public static ServerPageController getInstance(){
        if(instance == null)
            return instance = new ServerPageController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataButton.setOnAction(event -> {
            ServerDataPageController.getInstance().setAsScene();
        });
    }
}
