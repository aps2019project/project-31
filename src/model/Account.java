package model;

import constants.CardType;
import controller.CollectionMenu;
import view.Output;

import java.util.ArrayList;
import java.util.Collections;

public class Account {
    private static ArrayList<Account> allAccounts = new ArrayList<>();
    private static Deck editingDeck;
    private static Account mainAccount;

    private ArrayList<Card> collection = new ArrayList<>();
    private int daric;
    private ArrayList<Deck> decks = new ArrayList<>();
    private Deck theMainDeck;
    private String username;
    private String password;
    private ArrayList<MatchHistory> matchHistories = new ArrayList<>();
    private int[] winLoseDraw = new int[3];
    public String getPassword() {
        return password;
    }

    public Deck getTheMAinDeck(){
        return theMainDeck;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Account getMainAccount() {
        return mainAccount;
    }

    public static void setMainAccount(Account mainAccount) {
        Account.mainAccount = mainAccount;
    }

    public Account(String username, String password, int daric) {
        this.username = username;
        this.password = password;
        this.daric = daric;
        allAccounts.add(this);
    }

    public static Account createAccount(String username, String password) {
        return new Account(username, password, Integer.MAX_VALUE);
    }

    public ArrayList<Card> getCollection() {
        return collection;
    }

    public int getDaric() {
        return daric;
    }

    public void addMatchHistories(MatchHistory matchHistory) {
        matchHistories.add(matchHistory);
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public Deck getTheMainDeck() {
        return theMainDeck;
    }

    public ArrayList<MatchHistory> getMatchHistories() {
        return matchHistories;
    }

    public static boolean checkLogin(String username, String password) {
        Account account = findAccount(username);
        return account != null && account.password.equals(password);
    }

    public static Account findAccount(String username) {
        for (Account account : allAccounts) {
            if (account.username.equals(username)) {
                return account;
            }
        }
        return null;
    }

    public void addDaric(int add) {
        daric += add;
    }

    public void decreaseDaric(int decrease) {
        daric -= decrease;
    }

    public String getUsername() {
        return username;
    }

    public int[] getWinLoseDraw() {
        return winLoseDraw;
    }

    public static void addAccount(Account account) {
        allAccounts.add(account);
    }

    @Override
    public String toString() {
        return "UserName : " + username + " - Wins : " + winLoseDraw[0] + "\n";
    }


    public static void sortAllAccounts() {
        for (int i = 0; i < allAccounts.size(); i++) {
            int indexOfBest = i;
            for (int j = i + 1; j < allAccounts.size(); j++) {
                if (allAccounts.get(i).winLoseDraw[0] < allAccounts.get(j).winLoseDraw[0]) {
                    indexOfBest = j;
                }
            }
            if (indexOfBest != i) {
                Collections.swap(allAccounts, indexOfBest, i);
            }
        }
    }

    public static ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public void setTheMainDeck(Deck theMainDeck) {
        this.theMainDeck = theMainDeck;
    }

    public void createDeck(String name) {
        Deck deck = new Deck(name);
        addDeck(deck);
        editingDeck = deck;
    }

    private static void addCardToDeck(Card card) {
        if (editingDeck == null) {
            System.err.println("editing deck is null");
            return;
        }
        if (editingDeck.getCards().size() <= 17 && editingDeck.numberOfCardInDeck(card) < 3) {
            editingDeck.addCard(card);
            System.err.println("The card with id: " + card.getId() + " added");
        }
        if (card.getType() == CardType.hero && editingDeck.getHero() == null) {
            editingDeck.setHero((Hero) card);
            System.err.println("Hero added");
        }
    }

    public void addCardsToDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        for (String number : numbers) {
            Card card = CollectionMenu.findCardByIdInCollection(Integer.valueOf(number));
            if (card != null)
                addCardToDeck(card);
            else {
                Output.print("card :" + number + " is not in your collection");
            }
        }
    }

    public void removeCardsFromDeck(String[] numbers, String deckName) {
        selectDeck(deckName);
        if (editingDeck == null) {
            System.err.println("editing deck is null");
            return;
        }
        for (String number : numbers) {
            Card card = CollectionMenu.findCardByIdInCollection(Integer.valueOf(number));
            if (card != null) {
                try {
                    editingDeck.getCards().remove(card);
                } catch (Exception e) {
                    Output.notInDeck();
                }
                if (card.getType() == CardType.hero)
                    editingDeck.setHero(null);
            }

        }
    }

    public void selectAsMainDeck(String deckName) {
        selectDeck(deckName);
        checkValidationOfDeck(deckName);
        if (editingDeck == null)
            return;
        if (editingDeck.checkIfValid())
            setTheMainDeck(editingDeck);
    }

    public void showDeckByName(String deckName) {
        selectDeck(deckName);
        if (editingDeck == null)
            return;
        editingDeck.show();
    }


    public void checkValidationOfDeck(String deckName) {
        selectDeck(deckName);
        if (editingDeck == null)
            return;
        Output.showValidationOfDeck(editingDeck.checkIfValid(), editingDeck);
    }

    public void selectDeck(String deckName) {
        if (findDeckByName(deckName) == null) {
            Output.thereIsntDeck(deckName);
            return;
        }
        editingDeck = findDeckByName(deckName);
    }

    public Deck findDeckByName(String deckName) {
        for (Deck deck : getDecks()) {
            if (deck.getDeckName().equals(deckName))
                return deck;
        }
        return null;
    }

    public void deleteDeck(String deckName) {
        if (findDeckByName(deckName) == null) {
            Output.thereIsntDeck(deckName);
            return;
        }
        getDecks().remove(findDeckByName(deckName));
    }
}
