package Server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;
import controller.BattleMenu;
import controller.Shop;
import model.Account;
import model.BattleManager;
import model.Card;
import model.Deck;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
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
    private static User waitingUserMode1;
    private static User waitingUserMode2;
    private static User waitingUserMode3;


    @Override
    public void run() {
        try {
            while (true) {
                String command = dataInputStream.readUTF();
                shopRequestStockHandler(command);

                handleCardBuyingRequest(command);

                handleCardSellingRequest(command);

                multiPlayerRequestHandler(command);

                handleLeaderBoardRequest(command);


                if (handleLogout(command)) break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean handleLogout(String command) {
        Pattern pattern = Pattern.compile(ServerStrings.LOGOUT);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()){
            this.account = null;
            this.authToken = -1;
            users.remove(this);
            YaGson yaGson = new YaGsonBuilder().create();
            Server.saveAllAccounts();
            Server.makeWaitForLoginThread(yaGson, socket);
            return true;
        }
        return false;
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

    private void multiPlayerRequestHandler(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.MULTIPLAYERREQUEST);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            GameMode gameMode = findGameMode(matcher.group(1));
            switch (gameMode) {
                case Domination:
                    if (waitingUserMode3 == null) {
                        waitingUserMode3 = this;
                    } else makeBattle(GameMode.Domination, waitingUserMode3, this);
                    break;
                case DeathMatch:
                    if (waitingUserMode1 == null) {
                        waitingUserMode1 = this;
                    } else makeBattle(GameMode.DeathMatch, waitingUserMode1, this);

                    break;
                case Flag:
                    if (waitingUserMode2 == null) {
                        waitingUserMode2 = this;
                    } else makeBattle(GameMode.Flag, waitingUserMode2, this);
                    break;
            }
        } else {
            System.out.println("ridi tu ferestadan dastur be user");
        }
    }

    private void makeBattle(GameMode gameMode, User user1, User user2) throws IOException {
        BattleMenu.setBattleManagerForMultiPlayer(user1.account, user2.account, findNumberOfFlags(gameMode),
                findNumberOfHavingFlags(gameMode), gameMode);
        user1.dataOutputStream.writeUTF(ServerStrings.MULTIPLAYERSUCCESS);
        Server.sendObject(BattleMenu.getBattleManager(),user1.dataOutputStream);
        user2.dataOutputStream.writeUTF(ServerStrings.MULTIPLAYERSUCCESS);
        Server.sendObject(BattleMenu.getBattleManager(),user2.dataOutputStream);
    }

    private boolean authorise(Matcher matcher) throws IOException {
        int authToken = Integer.parseInt(matcher.group(1));
        if (!(authToken == this.authToken)) {
            dataOutputStream.writeUTF("Error 401: Unauthorized!");
            return true;
        }
        return false;
    }



    private GameMode findGameMode(String gm) {
        if (gm.equals("DeathMatch"))
            return GameMode.DeathMatch;
        if (gm.equals("Flag"))
            return GameMode.Flag;
        if (gm.equals("Domination"))
            return GameMode.Domination;
        return null;
    }

    private int findNumberOfFlags(GameMode gameMode) {
        switch (gameMode) {
            case Flag:
                return BattleManager.PERMANENT;
            case DeathMatch:
                return BattleManager.PERMANENT;
            case Domination:
                return 11;
        }
        return BattleManager.PERMANENT;
    }

    private int findNumberOfHavingFlags(GameMode gameMode) {
        switch (gameMode) {
            case Flag:
                return 12;
            case DeathMatch:
                return BattleManager.PERMANENT;
            case Domination:
                return BattleManager.PERMANENT;
        }
        return BattleManager.PERMANENT;
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
