package model;

import controller.Shop;
import view.Input;

public class Initializer {
    public static void main(String[] args) {
        Shop.loadAllCards();
        Input.start();
    }
}
