package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Initializer;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {

    private static ChatRoomController chatRoom;
    @FXML
    private VBox messageBox;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    private Button backButton;

    public ChatRoomController getInstance() {
        if (chatRoom == null)
            chatRoom = new ChatRoomController();
        return chatRoom;
    }

    public void setAsScene(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ChatRoom.fxml"));
            Scene scene = new Scene(root);
            Initializer.setCurrentScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatRoom = this;
    }
}
