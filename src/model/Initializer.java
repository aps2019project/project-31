package model;

import controller.BattleMenu;
import controller.Shop;
import view.Input;

public class Initializer {
    private static BattleMenu battleMenu = new BattleMenu(1000, "Battle Menu");

    public static BattleMenu getBattleMenu() {
        return battleMenu;
    }

    public static void main(String[] args) {

        System.err.println("Initializing maps ...");
        Map.createTheMap();
        System.err.println("maps initialized");
        System.err.println("Loading cards ...");
        Shop.loadAllCards();
        System.err.println("cards loaded");
        Input.start();
    }
}
