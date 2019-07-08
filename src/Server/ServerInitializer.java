package Server;

import controller.LoginPageController;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Initializer;
import view.Input;

public class ServerInitializer extends Application {
    private static Stage primaryStage;
    private static Scene currentScene;

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
        primaryStage.setScene(scene);
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    private static void setPrimaryStage(Stage primaryStage) {
        ServerInitializer.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        ServerPageController.getInstance().setAsScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        Initializer.initialiseData();
        launch(args);
        Server.main(null);
//        Input.start();
    }
}
