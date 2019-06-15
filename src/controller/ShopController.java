package controller;

import constants.CardType;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
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
    public Pane shopLogPane;
    public Pane playerLogPane;
    public Button collectionButton;
    public VBox logVBox;
    public Button sellButton;
    public Tab heroesTab1;
    public ListView<DisplayableCard> heroesList1;
    public ListView<DisplayableCard> minionsList1;
    public ListView<DisplayableCard> spellsList1;
    public ListView<DisplayableCard> usablesList1;
    public TabPane collectionTabPane;
    public Label daricView;

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

    public void initializeShopItems(ArrayList cards, ListView<DisplayableCard> listView, double scale) {
        boolean first = true;
        DisplayableCard displayableCard1 = null;
        DisplayableCard displayableCard2 = null;
        VBox vBox = null;
        for (Object card : cards) {
            displayableCard1 = new DisplayableCard((Card) card, "");
            displayableCard1.setScaleX(scale);
            displayableCard1.setScaleY(scale);
            displayableCard1.setTranslateY(-200);
            displayableCard1.setMaxWidth(100);
            listView.setMaxSize(800, 400);

            displayableCard1.setBackground(Background.EMPTY);
//            listView.setMaxSize(700, 400);
//            displayableCard1.setMaxSize(100, 200);
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

    public void updateCollection(){
        updateCollectionOf(CardType.hero, heroesList1);
        updateCollectionOf(CardType.minion, minionsList1);
        updateCollectionOf(CardType.spell, spellsList1);
        updateCollectionOf(CardType.item, usablesList1);
    }
    public void updateDaricView(){
        daricView.setText(Integer.toString(Account.getMainAccount().getDaric()));
    }

    public void updateCollectionOf(CardType cardType, ListView<DisplayableCard> listView){
        ArrayList<Card> cards = Account.getMainAccount().getSpecificCards(cardType);
        listView.getItems().clear();
        initializeShopItems(cards, listView, 0.3);
    }

    public void displayMessage(String massage) {
        LoginPageController.getInstance().displayMessage(massage, 17, 2, logVBox);
    }

    private void buyCard(Card card) {
        if (Account.getMainAccount().getDaric() < card.getPrice()) {
            displayMessage("not enough money");
            return;
        } else {
            int numberOfCards = 0;
            for (Card c : Account.getMainAccount().getCollection()) {
                if (card.getName().equals(c.getName()))
                    numberOfCards++;
            }
            if (numberOfCards >= 3) {
                displayMessage("Not more than 3 cards");
                return;
            }
        }
        Account.getMainAccount().decreaseDaric(card.getPrice());
        Account.getMainAccount().getCollection().add(card);
        updateCollection();
        updateDaricView();
        displayMessage("" + card.getName() + " bought successfully");
    }

    public Card findCardInCollection(Card card) {
        Card theCard = null;
        for (Card c : Account.getMainAccount().getCollection()) {
            if (c.getName().equalsIgnoreCase(card.getName())) {
                theCard = c;
                break;
            }
        }
        return theCard;
    }

    private void sellCard(Card card) {
        Card theCard = findCardInCollection(card);
        if (theCard == null) {
            displayMessage("card not in collection");
            return;
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            try {
                deck.getCards().remove(theCard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Account.getMainAccount().addDaric(card.getPrice());
        Account.getMainAccount().getCollection().remove(theCard);
        updateCollection();
        updateDaricView();
        displayMessage("" + card.getName() + " sold successfully");
    }

    public void buyCardsByName(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    buyCard(card);
            }
        }
    }

    public void sellCardsByName(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    sellCard(card);
            }
        }
    }

    public void searchCardsByNames(String[] cardNames) {
        for (Card card : Shop.getAllCards()) {
            for (String name : cardNames) {
                if (name.equals(card.getName()))
                    Output.showCardIdAndStuff(card);
            }
        }
    }

    public Card searchCardByName(String cardName) {
        for (Card card : Shop.getAllCards()) {
            if (cardName.equalsIgnoreCase(card.getName()))
                return card;
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeShopItems(Shop.getAllHeroes(), heroesList, 0.5);
        initializeShopItems(Shop.getAllMinions(), minionsList, 0.5);
        initializeShopItems(Shop.getAllSpells(), spellsList, 0.5);
        initializeShopItems(Shop.getAllUsables(), usablesList, 0.5);
        updateCollection();
        updateDaricView();
        shopBackButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
        buyButton.setOnAction(event -> {
            if (Account.getMainAccount() == null) {
                displayMessage("you are not Logged in!");
                return;
            }
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            ListView listView = (ListView) tab.getContent();
            DisplayableCard displayableCard = null;
            Card card = null;
            if (listView != null) {
                displayableCard = (DisplayableCard) listView.getSelectionModel().getSelectedItem();
                card = displayableCard.getCard();
            }
            if (displayableCard != null) {
                buyCard(displayableCard.getCard());
                selectTab(collectionTabPane, card);
            }
        });
        sellButton.setOnAction(event -> {
            if (Account.getMainAccount() == null) {
                displayMessage("you are not Logged in!");
                return;
            }
            Tab tab = collectionTabPane.getSelectionModel().getSelectedItem();
            ListView listView = (ListView) tab.getContent();
            DisplayableCard displayableCard = null;
            if (listView != null) {
                displayableCard = (DisplayableCard) listView.getSelectionModel().getSelectedItem();
            }
            if (displayableCard != null) {
                sellCard(displayableCard.getCard());
            }
        });
        findButton.setOnAction(event -> search());
        searchText.setOnAction(event -> search());
    }

    private void selectTab(TabPane tabPane, Card card){
        switch (card.getType()) {
            case hero:
                tabPane.getSelectionModel().select(tabPane.getTabs().get(0));
                break;
            case minion:
                tabPane.getSelectionModel().select(tabPane.getTabs().get(1));
                break;
            case spell:
                tabPane.getSelectionModel().select(tabPane.getTabs().get(2));
                break;
            case item:
                tabPane.getSelectionModel().select(tabPane.getTabs().get(3));
                break;
        }
    }

    private void search() {
        String input = searchText.getText();
        Card card = searchCardByName(input);
        if (card == null) {
            displayMessage("card not found!!");
        } else {
            selectTab(tabPane, card);
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            ListView listView = (ListView) tab.getContent();
            listView.scrollTo(new DisplayableCard(card, ""));
        }
    }
}