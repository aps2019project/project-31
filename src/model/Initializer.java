package model;

import controller.BattleMenu;
import controller.Shop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Input;

public class Initializer extends Application {
    private static BattleMenu battleMenu = new BattleMenu(1000, "Battle Menu");

    public static BattleMenu getBattleMenu() {
        return battleMenu;
    }

    public static void main(String[] args) {
        System.err.println("Loading all accounts...");
        Account.loadAllAccounts();
        System.err.println("Loading custom decks...");
        SinglePlayer.loadCustomDecks();
        System.err.println("Loading story decks...");
        Story.loadStoryDecks();
        System.err.println("Initializing maps ...");
        Map.createTheMap();
        System.err.println("maps initialized");
        System.err.println("Loading cards ...");
        Shop.loadAllCards();
        System.err.println("cards loaded");
        launch(args);
        Input.start();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loginPage = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
        Scene loginPageScene = new Scene(loginPage,1080,720);
        primaryStage.setScene(loginPageScene);
        primaryStage.show();
    }

}
