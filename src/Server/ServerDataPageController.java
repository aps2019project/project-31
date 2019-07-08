package Server;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerDataPageController implements Initializable {
    private static ServerDataPageController instance = null;

    private static Scene scene;
    public TabPane tabPane;
    public Tab heroesTab;
    public ListView heroesList;
    public Tab minionsTab;
    public ListView minionsList;
    public Tab spellsTab;
    public ListView spellsList;
    public Tab usablesTab;
    public ListView usablesList;
    public TextField searchText;
    public Button findButton;
    public Button requestStock;
    public ScrollPane scrollPane;
    public Label label;

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ServerDataPageController.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Server.setCurrentScene(scene);
    }

    public static ServerDataPageController getInstance(){
        if(instance == null)
            return instance = new ServerDataPageController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
