package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.Card;
import model.DisplayableCard;
import model.Hero;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShopController implements Initializable {
    private static Scene scene;
    private static ShopController shop;
    public ListView<VBox> heroesList;
    public ListView<VBox> minionsList;
    public ListView<VBox> spellsList;
    public ListView usablesList;

    public static ShopController getInstance() {
        if (shop == null) {
            shop = new ShopController();
        }
        return shop;
    }

    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Shop.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    public void initializeShopItems(ArrayList cards, ListView<VBox> listView) {
        boolean first = true;
        DisplayableCard displayableCard1 = null;
        DisplayableCard displayableCard2 = null;
        VBox vBox = new VBox();
        for (Object card : cards) {
            if (first) {
                displayableCard1 = new DisplayableCard((Card) card, "");
                displayableCard1.setMaxHeight(50);
                first = false;
            } else {
                displayableCard2 = new DisplayableCard((Card) card, "");
                displayableCard2.setMaxHeight(50);
                vBox = new VBox(displayableCard1, displayableCard2);
                vBox.setMaxHeight(7200);
                listView.getItems().add(vBox);
                displayableCard1 = null;
                displayableCard2 = null;
                first = true;
            }
        }
        if (displayableCard1 != null) {
            listView.getItems().add(new VBox(displayableCard1));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeShopItems(Shop.getAllHeroes(), heroesList);
        initializeShopItems(Shop.getAllMinions(), minionsList);
        initializeShopItems(Shop.getAllSpells(), spellsList);
        initializeShopItems(Shop.getAllUsables(), usablesList);
    }
}