package model;

import controller.BattleMenu;
import controller.Shop;
import javafx.application.Application;
import javafx.scene.Group;
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
        Input.start();

    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
