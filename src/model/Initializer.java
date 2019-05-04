package model;

import controller.Shop;
import view.Input;

public class Initializer {
    public static void main(String[] args) {
        System.err.println("Loading all cards...");
        Shop.loadAllCards();
        Input.start();
    }
}
