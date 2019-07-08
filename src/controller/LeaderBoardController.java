package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Account;
import model.Client;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoardController implements Initializable {
    private static Scene scene;
    private static LeaderBoardController leaderBoard;
    @FXML
    public StackPane leaderBoardRootPane;
    public Button leaderBoardBackButton;
    public ScrollPane LeaderBoardScrollPane;
    public Label label;
    @FXML
    private Button refreshButton;
    private VBox currentVbox;

    public LeaderBoardController() {
    }

    public static LeaderBoardController getInstance() {
        if (leaderBoard == null) {
            leaderBoard = new LeaderBoardController();
        }
        return leaderBoard;
    }

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/LeaderBoard.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    public String updateLeaderBoard() {
        String ret = "\n\n";
        Account.sortAllAccounts();
        for (int i = 0; i < Integer.min(Account.getAllAccounts().size(), 10); i++) {
            ret += "       " + (i + 1) + ".    " + Account.getAllAccounts().get(i).toString() + "";
        }
        return ret;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leaderBoardBackButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
        refreshLeaderBoard();
        refreshButton.setOnAction(actionEvent -> refreshLeaderBoard());

    }

    public void refreshLeaderBoard() {
        try {
            currentVbox = Client.getClient().requestLeaderBoard();
            currentVbox.setPadding(new Insets(50,0,0,0));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(label, currentVbox);
            LeaderBoardScrollPane.setContent(stackPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
