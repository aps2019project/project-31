package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.*;
import view.Output;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShopController implements Initializable {
    private static Scene scene;
    private static ShopController shop;
    public ListView<DisplayableCard> heroesList;
    public ListView<DisplayableCard> minionsList;
    public ListView<DisplayableCard> spellsList;
    public ListView<DisplayableCard> usablesList;
    public Button shopBackButton;
    public Button findButton;
    public Button buyButton;
    public TextField searchText;
    public TabPane tabPane;

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

    public void initializeShopItems(ArrayList cards, ListView<DisplayableCard> listView) {
        boolean first = true;
        DisplayableCard displayableCard1 = null;
        DisplayableCard displayableCard2 = null;
        VBox vBox = null;
        for (Object card : cards) {
            displayableCard1 = new DisplayableCard((Card) card, "");
            displayableCard1.setBackground(Background.EMPTY);
            displayableCard1.setMaxHeight(0);
            listView.getItems().add(displayableCard1);
            /*if (first) {
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
            }*/
        }
        /*if (displayableCard1 != null) {
            vBox = new VBox(displayableCard1);
            vBox.setBackground(Background.EMPTY);
            listView.getItems().add(vBox);
        }*/
    }

    private void buyCard(Card card) {
        if (Account.getMainAccount().getDaric() < card.getPrice()) {
            Output.print("not enough money");
            return;
        } else {
            int numberOfCards = 0;
            for (Card c : Account.getMainAccount().getCollection()) {
                if (card.getName().equals(c.getName()))
                    numberOfCards++;
            }
            if (numberOfCards >= 3) {
                Output.print("Not more than 3 cards");
                return;
            }
        }
        Account.getMainAccount().decreaseDaric(card.getPrice());
        Account.getMainAccount().getCollection().add(card);
        Output.print("card :" + card.getName() + " bought successfully");
    }

    private static void sellCard(Card card) {
        Card theCard = null;
        for (Card c : Account.getMainAccount().getCollection()) {
            if (c.getName().equalsIgnoreCase(card.getName())) {
                theCard = c;
                break;
            }
        }
        if (theCard == null) {
            Output.print("card not in collection");
            return;
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            try {
                deck.getCards().remove(theCard);
            } catch (Exception e) {

            }
        }
        Account.getMainAccount().addDaric(card.getPrice());
        Account.getMainAccount().getCollection().remove(theCard);
        Output.print("sold successfully");
    }

    public void buyCardsByName(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    buyCard(card);
            }
        }
    }

    public static void sellCardsByName(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    sellCard(card);
            }
        }
    }

    public static void searchCardsByNames(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    Output.showCardIdAndStuff(card);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeShopItems(Shop.getAllHeroes(), heroesList);
        initializeShopItems(Shop.getAllMinions(), minionsList);
        initializeShopItems(Shop.getAllSpells(), spellsList);
        initializeShopItems(Shop.getAllUsables(), usablesList);
        shopBackButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
        buyButton.setOnAction(event -> {
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            ListView listView = (ListView) tab.getContent();
            DisplayableCard displayableCard = null;
            if (listView != null) {
                displayableCard = (DisplayableCard) listView.getSelectionModel().getSelectedItem();
            }
            if (displayableCard != null) {
                buyCard(displayableCard.getCard());
            }
        });
    }

}