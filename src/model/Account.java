package model;

import java.util.ArrayList;
import java.util.Collections;

public class Account {
    private static ArrayList<Account> allAccounts = new ArrayList<>();
    private static Account mainAccount;
    private ArrayList<Card> collection;
    private int daric;
    private ArrayList<Deck> decks;
    private Deck theMainDeck;
    private String username;
    private String password;
    private ArrayList<MatchHistory> matchHistories;
    private int[] winLoseDraw = new int[3];

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

    private static Account findAccount(String username) {
        for (Account account : allAccounts) {
            if (account.username.equals(username)) {
                return account;
            }
        }
        return null;
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

    public void showAccounts() {
        //WTF is there to show ???
    }


    public static void sortAllAccounts() {
        for (int i = 0; i < allAccounts.size(); i++) {
            int theBest = i;
            for (int j = i + 1; j < allAccounts.size(); j++) {
                if (allAccounts.get(i).winLoseDraw[0] < allAccounts.get(j).winLoseDraw[0]) {
                    theBest = j;
                }
            }
            if (theBest != i) {
                Collections.swap(allAccounts, theBest, i);
            }
        }
    }

    public static ArrayList<Account> getAllAccounts() {
        return allAccounts;
    }

    public static Account getMainAccount() {
        return mainAccount;
    }
}
