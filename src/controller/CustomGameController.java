package controller;

import com.sun.jdi.IntegerValue;
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
            if(gameMode.getValue()==null){
                LoginPageController.getInstance().displayMessage("Enter the Game Mode!!", 19, 5, vbox);
            }
            else if (gameMode.getValue().equals(modes[0])) {
                BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                        100, 100, GameMode.DeathMatch, 1);
                //Initializer.getBattleMenu().runTheGame();
            }
            else if (gameMode.getValue().equals(modes[1])) {
                if (numberOfFlagsTextField.getText().length() < 1 || numberOfFlagsLable.getText().equals("")) {
                    LoginPageController.getInstance().displayMessage("Enter the value of Number Of Flags", 19, 5, vbox);
                } else {
                    int numberOfFlags = 0;
                    try {
                        numberOfFlags = Integer.valueOf(numberOfFlagsTextField.getText());
                    } catch (Exception e) {
                        LoginPageController.getInstance().displayMessage("This is not a number in number of flags", 19, 5, vbox);
                    }
                    BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                            numberOfFlags, 100, GameMode.Domination, 1);
                    // Initializer.getBattleMenu().runTheGame();
                }
            }
            else if (gameMode.getValue().equals(modes[2])) {
                if (numberOfTurnWinTextField.getText().length() < 1 || numberOfTurnWinTextField.getText().equals(""))
                    LoginPageController.getInstance().displayMessage("Enter the value of Winning Turns", 19, 5, vbox);
                else {
                    int numberOfTurnsHavingFlagToWin = 0;
                    try {
                        numberOfTurnsHavingFlagToWin = Integer.valueOf(numberOfTurnWinTextField.getText());
                    } catch (Exception e) {
                        LoginPageController.getInstance().displayMessage("This is not a number in winning turns", 19, 5, vbox);
                    }
                    BattleMenu.setBattleManagerForSinglePLayer(BattleManagerMode.CustomGame, Account.getMainAccount(),
                            100, numberOfTurnsHavingFlagToWin, GameMode.Flag, 1);
                    // Initializer.getBattleMenu().runTheGame();
                }
            }
        });
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
