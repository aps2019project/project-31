package controller;

import Server.ServerStrings;
import constants.CardType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    public Button requestStock;

    public static ShopController getInstance() {
        if (shop == null) {
            shop = new ShopController();
        }
        return shop;
    }

    public void setAsScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Shop.fxml"));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            scene = new Scene(root, 1080, 720);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Initializer.setCurrentScene(scene);
    }

    public void initializeShopItems(ArrayList cards, ListView<DisplayableCard> listView, double scale, int translateY) {
        DisplayableCard displayableCard1;
        listView.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0)
                event.consume();
        });

        listView.getItems().clear();
        for (Object card : cards) {
            displayableCard1 = new DisplayableCard((Card) card, "");
            displayableCard1.setScaleX(scale);
            displayableCard1.setScaleY(scale);

            displayableCard1.setTranslateY(translateY);

            displayableCard1.setBackground(Background.EMPTY);
            displayableCard1.setMaxSize(0, 0);
            displayableCard1.setBorder(Border.EMPTY);

            listView.getItems().add(displayableCard1);
        }
    }

    public void updateCollection(double scale, int translateY) {
        updateCollectionOf(CardType.hero, heroesList1, scale, translateY);
        updateCollectionOf(CardType.minion, minionsList1, scale, translateY);
        updateCollectionOf(CardType.spell, spellsList1, scale, translateY);
        updateCollectionOf(CardType.item, usablesList1, scale, translateY);
    }

    public void updateDaricView() {
        if (Account.getMainAccount() == null || daricView == null) {
            return;
        }
        daricView.setText(Integer.toString(Account.getMainAccount().getDaric()));
    }

    public void updateCollectionOf(CardType cardType, ListView<DisplayableCard> listView, double scale, int translateY) {
        if (Account.getMainAccount() == null || listView == null) {
            System.err.println(Account.getMainAccount());
            return;
        }
        ArrayList<Card> cards = Account.getMainAccount().getSpecificCardsOf(cardType, Account.getMainAccount().getCollection());
        listView.getItems().clear();
        initializeShopItems(cards, listView, scale, translateY);
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
        try {
            boolean wasSuccess = Client.getClient().requestCardBuy(card.getId());
            if (!wasSuccess){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ServerStrings.OUT_OF_STOCK);
                alert.show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Account.getMainAccount().decreaseDaric(card.getPrice());
        Account.getMainAccount().getCollection().add(card);
        updateCollection(0.4, -80);
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
        try {
            boolean wasSuccess = Client.getClient().requestCardSell(card.getId());
            if (!wasSuccess) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Unsuccessful");
                alert.show();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            try {
                for (int i = 0; i < deck.getCards().size(); i++)
                    deck.getCards().remove(theCard);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (deck.getHero() != null)
                if (deck.getHero().getName().equalsIgnoreCase(card.getName()))
                    deck.setHero(null);
            if (deck.getItem() != null)
                if (deck.getItem() != null && deck.getItem().getName().equalsIgnoreCase(card.getName()))
                    deck.setItem(null);
        }
        if (Account.getMainAccount().getMainDeck() != null
                && !Account.getMainAccount().getMainDeck().checkIfValid()) {
            displayMessage("main deck is no longer valid select a new one");
        }
        Account.getMainAccount().addDaric(card.getPrice());
        Account.getMainAccount().getCollection().remove(theCard);
        updateCollection(0.4, -80);
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
        initializeShopItems(Shop.getAllHeroes(), heroesList, 0.6, -10);
        initializeShopItems(Shop.getAllMinions(), minionsList, 0.6, -10);
        initializeShopItems(Shop.getAllSpells(), spellsList, 0.6, -10);
        initializeShopItems(Shop.getAllUsables(), usablesList, 0.6, -10);
        updateCollection(0.4, -80);
        updateDaricView();
        shopBackButton.setOnAction(event -> {
            MainMenuController.getInstance().setAsScene();
            updateCollection(0.4, -80);
        });

        requestStock.setOnAction(actionEvent -> {
            try {
                String string = Client.getClient().requestCardStock(getCardFromTab().getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(string);
                alert.show();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Select a card first!");
                alert.show();
            }
        });

        buyButton.setOnAction(event -> {
            if (Account.getMainAccount() == null) {
                displayMessage("you are not Logged in!");
                return;
            }
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            DisplayableCard displayableCard = null;
            ListView listView = (ListView) tab.getContent();
            Card card = null;

            if (listView != null) {
                displayableCard = (DisplayableCard) listView.getSelectionModel().getSelectedItem();
            }
            if (displayableCard != null) {
                card = displayableCard.getCard();
                buyCard(displayableCard.getCard());
                selectTab(collectionTabPane, card);
            }
            updateCollection(0.4, -80);
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
            updateCollection(0.4, -80);
        });
        findButton.setOnAction(event -> search());
        searchText.setOnAction(event -> search());
        collectionButton.setOnAction(event -> {
            CollectionController.getInstance().setAsScene();
        });
    }

    private Card getCardFromTab() {
        return ((DisplayableCard) ((ListView) tabPane.
                getSelectionModel().getSelectedItem()
                .getContent()).getSelectionModel().getSelectedItem()).getCard();
    }

    public void selectTab(TabPane tabPane, Card card) {
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