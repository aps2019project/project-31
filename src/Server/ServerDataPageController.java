package Server;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Account;

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
    public Button back;
    public Button refresh;

    private VBox currentVBox;

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ServerDataPage.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, 1080, 720);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ServerInitializer.setCurrentScene(scene);
    }

    public static ServerDataPageController getInstance() {
        if (instance == null)
            return instance = new ServerDataPageController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        back.setOnAction(event -> ServerPageController.getInstance().setAsScene());
        refreshLeaderBoard();
        refresh.setOnAction(actionEvent ->
        {
            refreshLeaderBoard();
        });
    }

    public void refreshLeaderBoard() {
        try {
            currentVBox = getLeaderBoard();
            currentVBox.setPadding(new Insets(50,0,0,0));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(label, currentVBox);
            scrollPane.setContent(stackPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getLeaderBoard() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        Account.sortAllAccounts();
        accounts:
        for (Account account : Account.getAllAccounts()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(LoginPageController.getInstance().makeMainLabel(account.toString(), 17));

            for (User user : User.getUsers()) {
                if (user.getSocket().isConnected() && user.getAccount().equals(account)) {
                    Label label = new Label("Online");
                    label.setFont(Font.font(17));
                    label.setTextFill(Color.GREEN);
                    hBox.getChildren().add(label);
                    vBox.getChildren().add(hBox);
                    continue accounts;
                }
            }
            Label label = new Label("Offline");
            label.setFont(Font.font(17));
            label.setTextFill(Color.RED);
            hBox.getChildren().add(label);
            vBox.getChildren().add(hBox);
        }
        return vBox;
    }
}
