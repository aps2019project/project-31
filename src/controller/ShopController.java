package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    public ListView<VBox> usablesList;
    public Button shopBackButton;

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
        VBox vBox = null;
        for (Object card : cards) {
            if (first) {
                displayableCard1 = new DisplayableCard((Card) card, "");
                displayableCard1.setBackground(Background.EMPTY);
                displayableCard1.setMaxHeight(0);
                first = false;
            } else {
                displayableCard2 = new DisplayableCard((Card) card, "");
                displayableCard2.setMaxHeight(0);
                vBox = new VBox(displayableCard1, displayableCard2);
                vBox.setMaxHeight(200);
                vBox.setMaxWidth(80);
                vBox.setBackground(Background.EMPTY);
                listView.getItems().add(vBox);
                displayableCard1 = null;
                displayableCard2 = null;
                first = true;
            }
        }
        if (displayableCard1 != null) {
            vBox = new VBox(displayableCard1);
            vBox.setBackground(Background.EMPTY);
            listView.getItems().add(vBox);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeShopItems(Shop.getAllHeroes(), heroesList);
        initializeShopItems(Shop.getAllMinions(), minionsList);
        initializeShopItems(Shop.getAllSpells(), spellsList);
        initializeShopItems(Shop.getAllUsables(), usablesList);
        shopBackButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());

    }
}