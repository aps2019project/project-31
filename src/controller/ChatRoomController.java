package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.ChatMessage;
import model.Client;
import model.Initializer;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {

    private static ArrayList<String> messages = new ArrayList<>();

    public static ArrayList<String> getMessages() {
        return messages;
    }

    private static ChatRoomController chatRoom;
    @FXML
    private VBox messageBox;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    private Button backButton;

    public static ChatRoomController getInstance() {
        if (chatRoom == null)
            chatRoom = new ChatRoomController();
        return chatRoom;
    }

    public void setAsScene() {
        try {
            Client.getClient().enterChatRoom();
            Parent root = FXMLLoader.load(getClass().getResource("/ChatRoom.fxml"));
            Scene scene = new Scene(root);
            Initializer.setCurrentScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addNativeMessage(String message) {
        messageBox.getChildren().addAll(new ChatMessage(message, true));
    }

    public void addForeignMessage(String message) {
        messageBox.getChildren().addAll(new ChatMessage(message, false));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatRoom = this;
        backButton.setOnAction(actionEvent -> {
            Client.getClient().exitChatroom();
        });
        sendButton.setOnAction(actionEvent -> {
            Client.getClient().sendChatMessage(textField.getText());
            textField.clear();
        });
        textField.setOnAction(actionEvent -> {
            Client.getClient().sendChatMessage(textField.getText());
            textField.clear();
        });
    }
}
