package model;

import controller.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import view.Input;

import java.io.File;
import java.util.Random;

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
        initialiseData();
        launch(args);
        Input.start();
    }

    public static void initialiseData() {
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
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String musicPath = System.getProperty("user.dir") + "src/resources/assets/music/music_battlemap_duskfall.m4a";
        setPrimaryStage(primaryStage);
        Media sound = new Media(new File(musicPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        LoginPageController.getInstance().setAsScene();
        primaryStage.show();
    }

}
