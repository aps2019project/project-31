package Server;

import controller.Shop;
import model.Account;
import model.Card;
import model.Deck;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends Thread {
    private static ArrayList<User> users = new ArrayList<>();
    private Socket socket;
    private int authToken = -1;
    private Account account;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    @Override
    public void run() {
        try {
            while (true) {
                String command = dataInputStream.readUTF();
                shopRequestStockHandler(command);

                handleCardBuyingRequest(command);

                handleCardSellingRequest(command);

                handleLeaderBoardRequest(command);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLeaderBoardRequest(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.GET_LEADERBOARD);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            Account.sortAllAccounts();
            accounts:
            for (int i = 0; i < Integer.min(Account.getAllAccounts().size(), 10); i++) {
                String ret = "       " + (i + 1) + ".    " +
                        Account.getAllAccounts().get(i).toString();
                dataOutputStream.writeUTF(ret);
                for (User user : users) {
                    if (user.socket.isConnected()
                            && user.account.equals(Account.getAllAccounts().get(i))) {
                        dataOutputStream.writeUTF("Online");
                        continue accounts;
                    }
                }
                dataOutputStream.writeUTF("Offline");
            }
            dataOutputStream.writeUTF("end");
        }
    }

    private void handleCardSellingRequest(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.REQUEST_SELL);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            Card card = Shop.findCardById(Integer.parseInt(matcher.group(2)));
            assert card != null;
            for (Deck deck : this.account.getDecks()) {
                try {
                    for (int i = 0; i < deck.getCards().size(); i++)
                        deck.getCards().remove(card);
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
            this.account.addDaric(card.getPrice());
            this.account.getCollection().remove(card);
            Shop.getStock().put(card.getId(), Shop.getStock().get(card.getId()) + 1);
            dataOutputStream.writeUTF(ServerStrings.SOLD);
        }

    }

    private void handleCardBuyingRequest(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.REQUEST_BUY);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            Card card = Shop.findCardById(Integer.parseInt(matcher.group(2)));
            assert card != null;
            if (Shop.getStock().get(card.getId()) == 0) {
                dataOutputStream.writeUTF(ServerStrings.OUT_OF_STOCK);
                return;
            }
            this.account.decreaseDaric(card.getPrice());
            this.account.getCollection().add(card);
            Shop.getStock().put(card.getId(), Shop.getStock().get(card.getId()) - 1);
            dataOutputStream.writeUTF(ServerStrings.BOUGHT);
        }
    }

    private void shopRequestStockHandler(String command) throws IOException {

        Pattern pattern = Pattern.compile(ServerStrings.REQUEST_STOCK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            Card card = Shop.findCardById(Integer.parseInt(matcher.group(2)));
            if (card == null) {
                dataOutputStream.writeUTF("Invalid card ID!");
            } else {
                dataOutputStream.writeUTF(card.getName() +
                        ": " +
                        Shop.getStock().get(card.getId()));
            }
        }
    }

    private boolean authorise(Matcher matcher) throws IOException {
        int authToken = Integer.parseInt(matcher.group(1));
        if (!(authToken == this.authToken)) {
            dataOutputStream.writeUTF("Error 401: Unauthorized!");
            return true;
        }
        return false;
    }

    private void multiPlayerRequestHandler() {

    }

    public User(Socket socket, Account account) {
        this.socket = socket;
        this.account = account;
        users.add(this);
        authToken = generateAuthToken();
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getAuthToken() {
        return authToken;
    }

    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private int generateAuthToken() {
        return Math.abs(account.getUsername().hashCode());
    }
}
