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
import javafx.scene.layout.VBox;
import model.*;
import view.Output;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CollectionController implements Initializable {
    private static CollectionController collection;
    private static Scene scene;

    public Button selectDeckButton;
    public ListView<Label> decksListView;
    public TextField newDeckName;
    public Button addNewDeckButton;
    public VBox collectionLog;
    public Button setAsMainButton;
    public Button mainMenuButton;
    public Button shopButton;
    public Button deleteDeckButton;
    public ListView<DisplayableCard> usablesList1;
    public ListView<DisplayableCard> spellsList1;
    public ListView<DisplayableCard> minionsList1;
    public ListView<DisplayableCard> heroesList1;
    public Button deleteButton;
    public Button addButton;
    public TextField searchText;
    public ListView<DisplayableCard> usablesList;
    public ListView<DisplayableCard> spellsList;
    public ListView<DisplayableCard> minionsList;
    public ListView<DisplayableCard> heroesList;
    public TabPane deckTabPane;
    public TabPane collectionTabPane;
    public Label deckLabel;

    public static CollectionController getInstance() {
        if (collection == null)
            collection = new CollectionController();
        return collection;
    }

    public CollectionController() {
    }

    public void setAsScene() {
        if (true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/collection.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    private void updateCollection() {
        ShopController.getInstance().updateCollectionOf(CardType.hero, heroesList);
        ShopController.getInstance().updateCollectionOf(CardType.minion, minionsList);
        ShopController.getInstance().updateCollectionOf(CardType.spell, spellsList);
        ShopController.getInstance().updateCollectionOf(CardType.item, usablesList);
    }

    private void updateEditingDeck() {
        if (Account.getEditingDeck() == null) {
            heroesList1.getItems().clear();
            minionsList1.getItems().clear();
            spellsList1.getItems().clear();
            usablesList1.getItems().clear();
            return;
        }
        try {
            updateDeckOf(CardType.minion, minionsList1);
            updateDeckOf(CardType.spell, spellsList1);

            if (Account.getEditingDeck().getHero() != null)
                ShopController.getInstance().initializeShopItems(new ArrayList<>(Collections.singletonList(Account.getEditingDeck().getHero())), heroesList1, 0.3, -210);
            else heroesList1.getItems().clear();
            if (Account.getEditingDeck().getItem() != null)
                ShopController.getInstance().initializeShopItems(new ArrayList<>(Collections.singletonList(Account.getEditingDeck().getItem())), usablesList1, 0.3, -210);
            else usablesList1.getItems().clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateDeckList() {
        decksListView.getItems().clear();
        for (Deck deck : Account.getMainAccount().getDecks()) {
            decksListView.getItems().add(new Label(deck.getDeckName()));
        }
    }

    public void updateDeckOf(CardType cardType, ListView<DisplayableCard> listView) {
        if (Account.getMainAccount() == null || listView == null) {
            System.err.println(Account.getMainAccount());
            return;
        }
        ArrayList<Card> cards = Account.getMainAccount().getSpecificCardsOf(cardType, Account.getEditingDeck().getCards());
        listView.getItems().clear();
        ShopController.getInstance().initializeShopItems(cards, listView, 0.3, -210);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateCollection();
        updateDeckList();
        addNewDeckButton.setOnAction(event -> {
            if (newDeckName.getText() == null) {
                displayMessage("enter name");
                return;
            }
            for (Label label : decksListView.getItems()) {
                if (label.getText().equalsIgnoreCase(newDeckName.getText())) {
                    displayMessage("repetitive name!");
                    return;
                }
            }
            Account.getMainAccount().addDeck(new Deck(newDeckName.getText()));
            decksListView.getItems().add(new Label(newDeckName.getText()));
            newDeckName.clear();
        });
        selectDeckButton.setOnAction(event -> {
            if (decksListView.getSelectionModel().getSelectedItem() == null) {
                displayMessage("select a deck");
                return;
            }
            Account.setEditingDeck(findDeckByName(decksListView.getSelectionModel().getSelectedItem().getText()));
            deckLabel.setText(Account.getEditingDeck().getDeckName());
            updateEditingDeck();
        });
        addButton.setOnAction(event -> {
            if (Account.getEditingDeck() == null) {
                displayMessage("select deck");
                return;
            }
            if (collectionTabPane.getSelectionModel().getSelectedItem() == null) {
                displayMessage("select a tab first");
                return;
            }
            ListView listView = (ListView) collectionTabPane.getSelectionModel().getSelectedItem().getContent();
            DisplayableCard displayableCard;
            if (listView.getSelectionModel().getSelectedItems() == null) {
                displayMessage("select card fist");
                return;
            }
            displayableCard = (DisplayableCard) listView.getSelectionModel().getSelectedItem();
            if (displayableCard != null) {
                Card card = displayableCard.getCard();
                ShopController.getInstance().selectTab(deckTabPane, displayableCard.getCard());
                if (displayableCard.getCard().getType() == CardType.hero) {
                    if (Account.getEditingDeck().getHero() == null) {
                        Account.getEditingDeck().addDisplayableCard(displayableCard);
                    } else {
                        displayMessage("delete current hero");
                        return;
                    }
                } else if (displayableCard.getCard().getType() == CardType.item) {
                    if (Account.getEditingDeck().getItem() == null)
                        Account.getEditingDeck().addDisplayableCard(displayableCard);
                    else {
                        displayMessage("delete current item");
                        return;
                    }
                } else {
                    int count = 0;
                    for (Card c : Account.getMainAccount().getCollection()) {
                        if (c.getName().equalsIgnoreCase(card.getName()))
                            count++;
                    }
                    for (Card c : Account.getEditingDeck().getCards()) {
                        if (c.getName().equalsIgnoreCase(card.getName())) {
                            count--;
                        }
                    }
                    if (count <= 0) {
                        displayMessage("buy some first");
                        return;
                    }
                    if (Account.getEditingDeck().getCards().size() < 18)
                        Account.getEditingDeck().addDisplayableCard(displayableCard);
                    else {
                        displayMessage("delete some cards first");
                        return;
                    }
                }
                updateEditingDeck();
            }
        });
        deleteDeckButton.setOnAction(event -> {
            if (Account.getEditingDeck() == null) {
                displayMessage("select a deck");
                return;
            }
            for (Label label : decksListView.getItems()) {
                if (label.getText().equalsIgnoreCase(Account.getEditingDeck().getDeckName())) {
                    decksListView.getItems().remove(label);
                    break;
                }
            }
            Account.getMainAccount().deleteDeck(Account.getEditingDeck().getDeckName());
            Account.setEditingDeck(null);
            decksListView.getSelectionModel().clearSelection();
            updateEditingDeck();
            updateDeckList();
        });
        deleteButton.setOnAction(event -> {
            if (deckTabPane.getSelectionModel().getSelectedItem().getContent() == null) {
                displayMessage("select a tab first");
                return;
            }
            ListView listView = (ListView) deckTabPane.getSelectionModel().getSelectedItem().getContent();
            if (listView.getSelectionModel().getSelectedItems().size() == 0) {
                displayMessage("select a card first");
                return;
            }
            Account.getEditingDeck().deleteDisplayableCards(listView.getSelectionModel().getSelectedItems());
            updateEditingDeck();
        });
        setAsMainButton.setOnAction(event -> {
            if (!Account.getEditingDeck().checkIfValid()) {
                displayMessage("deck is not valid!!");
                return;
            }
            Account.getMainAccount().selectAsMainDeck(Account.getEditingDeck().getDeckName());
            displayMessage("Main Deck is set \"" + Account.getEditingDeck().getDeckName() + "\"");
        });
        mainMenuButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
        shopButton.setOnAction(event -> ShopController.getInstance().setAsScene());
    }

    public void displayMessage(String massage) {
        LoginPageController.getInstance().displayMessage(massage, 15, 2, collectionLog);
    }

    public void showAllDecks() {
        try {
            Account.getMainAccount().getTheMainDeck().show();
        } catch (NullPointerException e) {
            System.err.println("main deck not initialized yet");
        }
        for (Deck deck : Account.getMainAccount().getDecks()) {
            if (deck != Account.getMainAccount().getTheMainDeck())
                deck.show();
        }
    }

    public void showAllDeckNames() {
        for (Deck deck : Account.getMainAccount().getDecks()) {
            Output.print(deck.getDeckName());
        }
    }

    public Card findCardByIdInCollection(int cardId) {
        for (Card card : Account.getMainAccount().getCollection()) {
            if (card != null && cardId == card.getId())
                return card;
        }
        return null;
    }

    public Deck findDeckByName(String name) {
        for (Deck deck : Account.getMainAccount().getDecks()) {
            if (deck.getDeckName().equalsIgnoreCase(name)) {
                displayMessage("deck found");
                return deck;
            }
        }
        return null;
    }

    public void showCardsByNames(String[] names) {
        for (Card card : Account.getMainAccount().getCollection()) {
            for (String name : names) {
                if (card != null && card.getName().equals(name))
                    card.show();
            }
        }
    }

    public static void showAllMyCards() {
        for (Card card : Account.getMainAccount().getCollection()) {
            Output.print(card.toString());
        }
    }
}
