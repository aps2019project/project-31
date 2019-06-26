package model;

import controller.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Input;

public class Initializer extends Application {
    private static Stage primaryStage;
    private static Scene currentScene;
    private static BattleMenu battleMenu = new BattleMenu(1000, "Battle Menu");

    public static BattleMenu getBattleMenu() {
        return battleMenu;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Initializer.primaryStage = primaryStage;
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void main(String[] args) {
        System.err.println("Loading all accounts...");
        Account.loadAllAccounts();
        System.err.println("Loading custom decks...");
        SinglePlayer.loadCustomDecks();
        System.err.println("Loading story decks...");
        Story.loadStoryDecks();
        System.err.println("Loading cards ...");
        Shop.loadAllCards();
        System.err.println("Initializing maps ...");
        Map.getInstance().createTheMap();
        System.err.println("cards loaded");
        launch(args);
        Input.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        LoginPageController.getInstance().setAsScene();
        primaryStage.show();
    }

}
