package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    public static Scene scene = null;
    private static LoginPageController loginPage;

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
    @FXML
    private StackPane infoPane;

    public LoginPageController() {
    }

    public static LoginPageController getInstance() {
        if (loginPage == null) {
            loginPage = new LoginPageController();
        }
        return loginPage;
    }

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 3 / 5, screenWidth * 2 / 5);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getResource("/assets/fonts/Lato-Regular.ttf").toExternalForm(), 10);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Double screenWidth = screen.getWidth();
        double scaleX = screenWidth / mainContainer.getPrefWidth() * 3 / 5;
        mainContainer.setScaleX(scaleX);
        mainContainer.setScaleY(scaleX);

        System.out.println(Shop.getAllCollectibles().get(3).isCollectible());

        exitButton.setOnAction(actionEvent -> {
            Initializer.getPrimaryStage().close();
        });
        loginButton.setOnAction(actionEvent -> {
            login();
        });

        infoButton.setOnAction(actionEvent -> {
            Image image = new Image(getClass().getResource("/images/Credits.jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            infoPane.getChildren().add(imageView);
            imageView.setFitHeight(720);
            imageView.setFitWidth(360);
            imageView.setOpacity(0);
            AnimationTimer animationTimer = new AnimationTimer() {
                long time = 0;

                @Override
                public void handle(long l) {
                    if (time == 0) {
                        time = l;
                    }
                    if (l - time > 1000000) {
                        time = l;
                        imageView.setOpacity(imageView.getOpacity() + 0.01);
                    }
                    if (imageView.getOpacity() >= 1)
                        this.stop();
                }
            };
            animationTimer.start();
            imageView.setOnMouseClicked(mouseEvent -> {
                animationTimer.stop();
                AnimationTimer removeAnimationTimer = new AnimationTimer() {
                    long time = 0;

                    @Override
                    public void handle(long l) {
                        if (time == 0) {
                            time = l;
                        }
                        if (l - time > 10000000) {
                            time = l;
                            imageView.setOpacity(imageView.getOpacity() - 0.01);
                        }
                        if (imageView.getOpacity() <= 0) {
                            infoPane.getChildren().remove(imageView);
                        }
                    }
                };
                removeAnimationTimer.start();
            });
        });
        signupButton.setOnAction(actionEvent -> createAccount());
    }

    public void login() {
        String username = usernameTF.getText();
        Account account = Account.findAccount(username);
        if (account == null) {
            displayMessage("Incorrect Username!", 17, 2, infoVBox);
            return;
        }
        String password = passwordField.getText();
        if (account.getPassword().equals(password)) {
            Account.setMainAccount(account);
            displayMessage("Login Successful! Entering game...", 17, 2, infoVBox);
            //handle entering the next scene
            MainMenuController.getInstance().setAsScene();
            usernameTF.clear();
            passwordField.clear();
            System.err.println(Account.getMainAccount().getMainDeck().getDeckName());
            return;
        } else {
            displayMessage("Incorrect Password!", 17, 2, infoVBox);
            return;
        }
    }

    public void createAccount() {
        String username = signupUsername.getText();
        System.err.println("checking..");
        if (signupUsername.getText().isEmpty() || signupPassword.getText().isEmpty()) {
            displayMessage("Enter account info!", 17, 2, infoVBox);
            return;
        }
        if (Account.findAccount(username) != null) {
            displayMessage("Username already taken!", 17, 2, infoVBox);
            return;
        }
        String password = signupPassword.getText();
        String passRepeat = repassword.getText();
        if (!passRepeat.equals(password)) {
            displayMessage("Passwords must match!", 17, 2, infoVBox);
            return;
        }
        Account.createAccount(username, password.trim());
        signupUsername.clear();
        signupPassword.clear();
        repassword.clear();
        displayMessage("Account created! Sign in!", 17, 2, infoVBox);
    }


    public void displayMessage(String text, int fontSize, long delayTime, Pane pane) {
        Label label = makeMainLabel(text, fontSize);
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
        if (pane.getChildren().size() > 3) {
            pane.getChildren().remove(0);
        }
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
