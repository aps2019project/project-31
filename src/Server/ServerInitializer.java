package Server;

import controller.LoginPageController;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Initializer;
import view.Input;

public class ServerInitializer extends Application {
    public static void setCurrentScene(Scene scene) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        LoginPageController.getInstance().setAsScene();
        primaryStage.show();
    }

    private static void setPrimaryStage(Stage primaryStage) {

    }
    public static void main(String[] args) {
        Initializer.initialiseData();
        Server.main(null);
        launch(args);
        Input.start();
    }
}
