package controller;

import constants.CardType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardMakerController implements Initializable {
    @FXML
    private VBox displayVBox;
    @FXML
    private Label messageLabel;
    @FXML
    private ScrollPane selectionPane;
    @FXML
    private VBox selectionVBox;
    @FXML
    private Button minionButton;
    @FXML
    private Button heroButton;
    @FXML
    private Button spellButton;
    @FXML
    private Button itemButton;

    @FXML
    private VBox centerVBox;
    @FXML
    private ImageView backgroundImage;


    private static Scene cardMakerScene;
    private static CardMakerController cardMaker;

    private CardType currentCardType;
    private String currentCardName;
    @FXML
    private StackPane mainContainer;

    public static CardMakerController getInstance() {
        if (cardMaker == null) {
            cardMaker = new CardMakerController();
        }
        return cardMaker;
    }

    public void setAsScene() {
        if (cardMakerScene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/CardMaker.fxml"));

                cardMakerScene = new Scene(root, Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 5,
                        Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2 / 5);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(cardMakerScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double scaleX = screenWidth / mainContainer.getPrefWidth() * 3 / 5;
        mainContainer.setScaleX(scaleX);
        mainContainer.setScaleY(scaleX);
        centerVBox.setScaleY(0.87);


        minionButton.setOnAction(actionEvent -> makeMinion());
    }

    private void makeMinion() {
        currentCardType = CardType.minion;
        messageLabel.setText("Enter card name!");

        TextField nameField = makeTextField();
        nameField.setOnAction(actionEvent -> {
            currentCardName = nameField.getText();
            selectionVBox.getChildren().remove(nameField);
        });
        selectionVBox.getChildren().addAll(nameField);

    }

    private TextField makeTextField() {
        TextField textField = new TextField();
        textField.getStylesheets().add(getClass().getResource("/style/inputBox.css").toExternalForm());
        textField.setPrefWidth(20);
        textField.setPrefHeight(30);
        return textField;
    }
}
