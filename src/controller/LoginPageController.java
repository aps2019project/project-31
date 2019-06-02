package controller;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Account;
import model.Initializer;
import view.Output;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    @FXML
    public HBox mainContainer;
    @FXML
    public TextField usernameTF;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public ImageView middlePicture;
    @FXML
    private TextField signupUsername;
    @FXML
    private PasswordField signupPassword;
    @FXML
    private PasswordField repassword;
    @FXML
    private Button signupButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button infoButton;
    @FXML
    private VBox infoVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitButton.setOnAction(actionEvent -> {
            Initializer.getPrimaryStage().close();
        });
        loginButton.setOnAction(actionEvent -> {
        });
        signupButton.setOnAction(actionEvent -> createAccount());
    }

    public void createAccount() {
        String username = signupUsername.getText();
        System.err.println("checking..");
        if (Account.findAccount(username) != null) {
            displayLabel("Username already taken!", 17, 2, infoVBox);
            return;
        }
        String password = signupPassword.getText();
        String passRepeat = repassword.getText();
        if (!passRepeat.equals(password)) {
            displayLabel("Passwords must match!", 17, 2, infoVBox);
            return;
        }
        Account account = Account.createAccount(username, password.trim());
        signupUsername.clear();
        signupPassword.clear();
        repassword.clear();
        displayLabel("Account created! Sign in!", 17, 2, infoVBox);
    }


    public void displayLabel(String text, int fontSize ,long delayTime, Pane pane) {
        Label label = makeMainLabel(text,fontSize);
        pane.getChildren().add(label);
        AnimationTimer animationTimer = new AnimationTimer() {
            long before = 0;
            long secondBefore = 0;

            @Override
            public void handle(long l) {
                if (before == 0) {
                    before = l;
                }
                if (l - before > delayTime * 1000 * 1000 * 1000 && secondBefore == 0) {
                    secondBefore = l;
                }
                if (l - secondBefore > 2000 && secondBefore != 0) {
                    secondBefore = l;
                    label.setOpacity(label.getOpacity() - 0.002);
                }
                if (label.getOpacity() <= 0) {
                    pane.getChildren().remove(label);
                }
            }
        };
        animationTimer.start();
        return;
    }

    public Label makeMainLabel(String text, int fontsize) {
        Label label = new Label(text);
        label.getStylesheets().add(getClass().getResource("/style/mainLabel.css").toExternalForm());
        label.setFont(Font.font(fontsize));
        label.setAlignment(Pos.CENTER);
        return label;
    }
}
