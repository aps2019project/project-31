package Server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;
import controller.BattleMenu;
import controller.Shop;
import model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends Thread {
    private static ArrayList<User> users = new ArrayList<>();
    private Socket socket;
    private int authToken = -1;
    private Account account;
    private DataInputStream is;
    private DataOutputStream os;
    private static User waitingUserMode1;
    private static User waitingUserMode2;
    private static User waitingUserMode3;
    private BattleManager battle;
    private BattleServer battleServer;


    private void goToBattle() throws IOException {
        System.out.println("we are in the battle :)");
        battleServer.updateBothUsers();
        while (true) {
            System.out.println("go to battle while loop");
            if (!battleServer.currentPlayer().getCommandFromCurrentPlayer()) {
                gameFinished();
                return;
            }
        }
    }


    private boolean getCommandFromCurrentPlayer() throws IOException {
        System.out.println("get command from current player");
        String command = is.readUTF();
        System.out.println("the command is : "+ command);
        while (!command.equals("T") || !command.equals(ServerStrings.CONCEDE)) {
            battleServer.gameCompiler.whatIsThePlay(command);
        //    battleServer.updateBothUsers();

            System.out.println("get the next command from current player");
            command=is.readUTF();
            System.out.println("the command is : "+ command);
        }
        if (command.equals(ServerStrings.CONCEDE)) {
            System.out.println("the concede has been received");


            return false;
        }
        System.out.println("the end turn has been received");
     //   battleServer.updateBothUsers();
        return true;
    }


    private void gameFinished() {

    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("run loop");
                String command = is.readUTF();
                System.out.println("user received:" + command);
                shopRequestStockHandler(command);

                handleCardBuyingRequest(command);

                handleCardSellingRequest(command);

                multiPlayerRequestHandler(command);

                handleLeaderBoardRequest(command);

                handleNewDeck(command);

                handleCardAddition(command);

                handleCancelRequest(command);

                handleDeckDeletion(command);

                handleCardRemoval(command);

                handleMainDeckSet(command);


                if (handleLogout(command)) break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMainDeckSet(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.SET_AS_MAIN_REQUEST);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            for (Deck deck : account.getDecks()) {
                if (deck.getDeckName().matches(matcher.group(1))) {
                    account.setMainDeck(deck);
                    os.writeUTF(ServerStrings.MAIN_DECK_SET);
                    return;
                }
            }
            safetyOutput("Nay!");
        }
    }

    private void handleCardRemoval(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.DELETE_CARD_REQUEST);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            int cardID = Integer.parseInt(matcher.group(1));
            String deckName = matcher.group(2);
            for (Deck deck : account.getDecks()) {
                System.out.println(deck.getDeckName());
                if (deck.getDeckName().equals(deckName)) {
                    deck.deleteCard(Shop.findCardById(cardID));
                    os.writeUTF(ServerStrings.CARD_DELETED);
                    return;
                }
            }
            safetyOutput("not grrr");
        }

    }

    private void handleDeckDeletion(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.DELETE_DECK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            account.deleteDeck(matcher.group(1));
            os.writeUTF(ServerStrings.DECK_DELETED);
        }
    }

    private void handleCardAddition(String command) {
        Pattern pattern = Pattern.compile(ServerStrings.ADD_CARD_TO_DECK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            for (Deck deck : account.getDecks()) {
                if (deck.getDeckName().equals(matcher.group(2))) {
                    deck.addCard(Shop.findCardById(Integer.parseInt(matcher.group(1))));
                    try {
                        os.writeUTF(ServerStrings.CARD_ADDED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            safetyOutput("Nooope");
        }

    }

    private void safetyOutput(String nooope) {
        try {
            os.writeUTF(nooope);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewDeck(String command) {
        Pattern pattern = Pattern.compile(ServerStrings.NEW_DECK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            this.account.getDecks().add(new Deck(matcher.group(1)));
            try {
                os.writeUTF(ServerStrings.NEW_DECK_SUCCESS);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            safetyOutput("Nooooope");
        }
    }

    public void handleCancelRequest(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.CANCELPLAYREQUEST);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            os.writeUTF(ServerStrings.CANCELSUCCESSFULLY);
            if (this == waitingUserMode1)
                waitingUserMode1 = null;
            if (this == waitingUserMode2)
                waitingUserMode2 = null;
            if (this == waitingUserMode3)
                waitingUserMode3 = null;
        }
    }

    public boolean handleLogout(String command) {
        Pattern pattern = Pattern.compile(ServerStrings.LOGOUT);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            System.out.println("logging out!");
            this.authToken = -1;
            users.remove(this);
            YaGson yaGson = new YaGsonBuilder().create();
            Server.saveAllAccounts();
            System.out.println("saved all accounts");
            Server.makeWaitForLoginThread(yaGson, socket);
            System.out.println("User " + account.getUsername() + "logged out!");
            this.account = null;
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
                os.writeUTF(ret);
                for (User user : users) {
                    if (user.socket.isConnected()
                            && user.account.equals(Account.getAllAccounts().get(i))) {
                        os.writeUTF("Online");
                        continue accounts;
                    }
                }
                os.writeUTF("Offline");
            }
            os.writeUTF("end");
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
            updateStockFile();
            os.writeUTF(ServerStrings.SOLD);
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
                os.writeUTF(ServerStrings.OUT_OF_STOCK);
                return;
            }
            this.account.decreaseDaric(card.getPrice());
            this.account.getCollection().add(card);
            Shop.getStock().put(card.getId(), Shop.getStock().get(card.getId()) - 1);
            updateStockFile();
            os.writeUTF(ServerStrings.BOUGHT);
        }
    }

    private synchronized static void updateStockFile() {
        try {
            YaGson yaGson = new YaGsonBuilder().create();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") +
                    "/Sources/ServerResources/serverData.txt"));
            bufferedWriter.write(yaGson.toJson(Shop.getStock()));
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void shopRequestStockHandler(String command) throws IOException {

        Pattern pattern = Pattern.compile(ServerStrings.REQUEST_STOCK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            Card card = Shop.findCardById(Integer.parseInt(matcher.group(2)));
            if (card == null) {
                os.writeUTF("Invalid card ID!");
            } else {
                os.writeUTF(card.getName() +
                        ": " +
                        Shop.getStock().get(card.getId()));
            }
        }
    }

    private void multiPlayerRequestHandler(String command) throws IOException {
        Pattern pattern = Pattern.compile(ServerStrings.MULTIPLAYERREQUEST);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            if (authorise(matcher)) return;
            GameMode gameMode = findGameMode(matcher.group(2));
            switch (gameMode) {
                case Domination:
                    System.out.println("domination it is");

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
            System.out.println("sina nooni e");
        }
    }


    private void makeBattle(GameMode gameMode, User user1, User user2) throws IOException {
        BattleMenu.setBattleManagerForMultiPlayer(user1.account, user2.account, findNumberOfFlags(gameMode),
                findNumberOfHavingFlags(gameMode), gameMode);
        battle = BattleMenu.getBattleManager();
        battle.initialTheGame();
        battleServer = new BattleServer(battle, user1, user2);
        user1.os.writeUTF(ServerStrings.MULTIPLAYERSUCCESS);
        user1.sendMapAndBattle();
        user2.os.writeUTF(ServerStrings.MULTIPLAYERSUCCESS);
        user2.sendMapAndBattle();

        goToBattle();


    }

    private boolean authorise(Matcher matcher) throws IOException {
        int authToken = Integer.parseInt(matcher.group(1));
        if (!(authToken == this.authToken)) {
            os.writeUTF("Error 401: Unauthorized!");
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
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
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

    public void sendMapAndBattle() {
        try {
            Server.sendObject(BattleMenu.getBattleManager(), os);
            sendMap();
            System.out.println("map and battle sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMap() throws IOException {
        for (int i = 1; i < Map.MAP_X1_LENGTH; i++) {
            for (int j = 1; j < Map.MAP_X2_LENGTH; j++) {
                sendOneCell(Map.getInstance().getCell(i, j));
            }
        }
    }

    private String boolToString(boolean bool) {
        if (bool == true)
            return "true";
        else return "false";
    }

    private void sendOneCell(Cell cell) throws IOException {
        os.writeUTF(cell.getX1Coordinate() + "");
        os.writeUTF(cell.getX2Coordinate() + "");
        Server.sendObject(cell.getCardInCell(), os);
        os.writeUTF(cell.getOnFireTurns() + "");
        os.writeUTF(cell.getOnPoisonTurns() + "");
        os.writeUTF(boolToString(cell.hasFlag()));
        Server.sendObject(cell.getItem(), os);
    }
}
