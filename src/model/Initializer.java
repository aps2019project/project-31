package model;

import controller.Shop;
import view.Input;

public class Initializer {
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
