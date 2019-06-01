package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    public HBox mainContainer;
    public TextField usernameTF;
    public PasswordField passwordField;
    public Button loginButton;
    public ImageView middlePicture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
