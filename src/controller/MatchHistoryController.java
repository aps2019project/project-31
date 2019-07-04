package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import model.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MatchHistoryController implements Initializable {
    private static MatchHistoryController instance;
    private static Scene scene;
    public Label playerName;
    public ListView listView;
    public Button backButton;
    public Button replayButton;

    public static MatchHistoryController getInstance() {
        if (instance == null)
            instance = new MatchHistoryController();
        return instance;
    }

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/MatchHistory.fxml"));
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

        instance = this;
        updateInfo();
        backButton.setOnAction(event -> {
            MainMenuController.getInstance().setAsScene();
        });
        replayButton.setOnAction(event -> {
            MatchHistory matchHistory = ((InfoHBox) listView.getSelectionModel().getSelectedItem()).getMatchHistory();
            GameRecord gameRecord = matchHistory.getGameRecord();
            gameRecord.makeFormalBattleManagerForRecord();
            Account.getMainAccount().setSelectedGameRecord(gameRecord);
            BattlePageController.getInstance().setAsScene();
            gameRecord.showTheWholeGame();
        });
    }
    private void updateInfo(){
        playerName.setText(Account.getMainAccount().getUsername());
        listView.getItems().clear();

        for (MatchHistory matchHistory : Account.getMainAccount().getMatchHistories()) {
            InfoHBox infoHBox = new InfoHBox();
            infoHBox.setMatchHistory(matchHistory);
            infoHBox.getChildren().addAll(new Label(matchHistory.getMe().getHero().getName()), new Label(matchHistory.getOpponent().getAccount().getUsername()), new Label(matchHistory.getGameMode().toString()), new Label(matchHistory.getOutcome()), new Label(matchHistory.figureTime()));
            infoHBox.setBackground(Background.EMPTY);
            listView.getItems().add(infoHBox);
        }
    }
}
