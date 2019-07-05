package controller;

import constants.BattleManagerMode;
import constants.GameMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Account;
import model.BattleManager;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomGameController implements Initializable {
    private static Scene scene;
    private static CustomGameController customGame;
    public ChoiceBox gameMode;
    public Label numberOfFlagsLable;
    public Button playButtonCustom;
    public TextField numberOfFlagsTextField;
    public Label numberOfTurnWinLable;
    public TextField numberOfTurnWinTextField;
    public Button back;
    public VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] modes = {"Death Match(1)", "Flag(2)", "Domination(3)"};
        ObservableList list = FXCollections.observableArrayList();
        list.addAll(modes);
        gameMode.getItems().addAll(list);
        back.setOnAction(event -> {
            PlayMenuController.getInstance().setAsScene();
        });
        playButtonCustom.setOnAction(event -> {
            if (gameMode.getValue() == null) {
                LoginPageController.getInstance().displayMessage("Enter the Game Mode!!", 19, 5, vbox);
            } else if (gameMode.getValue().equals(modes[0])) {
                customGameMode1();
            } else if (gameMode.getValue().equals(modes[1])) {
                customGameMode2();
            } else if (gameMode.getValue().equals(modes[2])) {
                customGameMode3();
            }
        });
    }

    private void customGameMode1() {
        BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                100, 100, GameMode.DeathMatch, 1);
        SinglePlayerBattlePageController.getInstance().setAsScene();
    }

    private void customGameMode2() {
        if (numberOfTurnWinTextField.getText().length() < 1 || numberOfTurnWinTextField.getText().equals("")) {
            LoginPageController.getInstance().displayMessage("Enter the value of Winning Turns", 19, 5, vbox);
        } else {
            int numberOfTurnsHavingFlagToWin = 0;
            try {
                numberOfTurnsHavingFlagToWin = Integer.valueOf(numberOfTurnWinTextField.getText());
            } catch (Exception e) {
                LoginPageController.getInstance().displayMessage("This is not a number in winning turns", 19, 5, vbox);
                return;
            }
            if (numberOfTurnsHavingFlagToWin < 20) {
                BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                        BattleManager.PERMANENT, numberOfTurnsHavingFlagToWin, GameMode.Flag, 1);
                SinglePlayerBattlePageController.getInstance().setAsScene();
            }else LoginPageController.getInstance().displayMessage("TOO BIG A NUMBER SIR!", 19, 5, vbox);
        }
    }

    private void customGameMode3() {
        if (numberOfFlagsTextField.getText().length() < 1 || numberOfFlagsTextField.getText().equals(""))
            LoginPageController.getInstance().displayMessage("Enter the value of number of Flags", 19, 5, vbox);
        else {
            int numberOfFlags = 0;
            try {
                numberOfFlags = Integer.valueOf(numberOfFlagsTextField.getText());
            } catch (Exception e) {
                LoginPageController.getInstance().displayMessage("This is not a number in number of flags", 19, 5, vbox);
                return;
            }
            if (numberOfFlags < 45) {
                BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                        numberOfFlags, 100, GameMode.Domination, 1);
                SinglePlayerBattlePageController.getInstance().setAsScene();
            } else LoginPageController.getInstance().displayMessage("TOO BIG A NUMBER SIR!", 19, 5, vbox);
        }
    }

    public static CustomGameController getInstance() {
        if (customGame == null) {
            customGame = new CustomGameController();
        }
        return customGame;
    }


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/CustomGame.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }
}
