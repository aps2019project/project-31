package model;

import Server.Server;
import Server.ServerStrings;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;
import controller.*;
import controller.LoginPageController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.net.Socket;
import java.util.AbstractCollection;
import java.util.HashMap;

public class Client extends Thread {
    private Socket socket;
    private static Client client;
    private static int authToken = -1;
    private DataOutputStream os;
    private DataInputStream is;

    public static int getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(int authToken) {
        Client.authToken = authToken;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        Client.client = client;
    }

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    public Account attemptLogin(String username, String password) throws IOException {
        os.writeUTF("login username:" + username +
                " password:" + password);
        String command = is.readUTF();
        if (command.matches(ServerStrings.LOGINSUCCESS)) {
            System.out.println("getting account...");
            YaGson yaGson = new YaGsonBuilder().create();

            Account account = (Account) receiveObject(is, Account.class);
            int auth = Integer.parseInt(is.readUTF());
            setAuthToken(auth);
            HashMap<Integer, Integer> stock = yaGson.fromJson(is.readUTF(), HashMap.class);
            Shop.setStock(stock);
            return account;
        } else return null;
    }

    public static Object receiveObject(DataInputStream is, Class objectClass) throws IOException {
        YaGson yaGson = new YaGsonBuilder().create();
        int size = Integer.parseInt(is.readUTF());
        byte[] bytes = new byte[size];

        for (int i = 0; i < size; i++) {
            byte b = is.readByte();
            bytes[i] = b;
        }
        String objectString = new String(bytes);
        return yaGson.fromJson(objectString, objectClass);

    }

    public void sendCancelRequest() {

        try {
            os.writeUTF(authToken + " request is canceled");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPlayRequest(GameMode gameMode) {
        try {
            switch (gameMode) {
                case DeathMatch:
                    os.writeUTF(authToken + " DeathMatch request from user:" + Account.getMainAccount().getUsername());
                    break;
                case Flag:
                    os.writeUTF(authToken + " Flag request from user:" + Account.getMainAccount().getUsername());
                    break;
                case Domination:
                    os.writeUTF(authToken + " Domination request from user:" + Account.getMainAccount().getUsername());
                    break;
            }
            new Thread(()->{
                try {
                     String serverReply = is.readUTF();
                    if (serverReply.equals(ServerStrings.MULTIPLAYERSUCCESS)) {
                        BattleManager battle = (BattleManager) receiveObject(this.is, BattleManager.class);
                        System.out.println("receeeeeeeeeeeeived successfully");
                        BattleMenu.setBattleManager(battle);
                        if(battle==null)
                            System.out.println("wtf isssssssssssssssssssssssssssssssssssssssssssssss");
                        WaitingPageController.getInstance().johnyJohnyYesPapaGoingToBattle.set(true);
                        synchronized (WaitingPageController.getInstance()) {
                            WaitingPageController.getInstance().notifyAll();
                        }
                    } else if(serverReply.equals(ServerStrings.CANCELSUCCESSFULLY)) {
                        WaitingPageController.getInstance().johnyJohnyYesPapaGoingToBattle.set(true);
                        System.out.println("we canceled the game honey");
                        synchronized (WaitingPageController.getInstance()){
                            WaitingPageController.getInstance().notifyAll();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String requestCardStock(int id) {
        try {
            os.writeUTF(authToken + " request card stock: " + id);
            return is.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean requestCardBuy(int id) throws IOException {
        os.writeUTF(authToken + " request to buy card: " + id);
        String response = is.readUTF();
        return response.matches(ServerStrings.BOUGHT);
    }

    public boolean requestCardSell(int id) throws IOException {
        os.writeUTF(authToken + " request to sell card: " + id);
        String response = is.readUTF();
        return response.matches(ServerStrings.SOLD);
    }

    public boolean requestCardAddition(int cardID, String deckname){
        try {
            os.writeUTF("add " + cardID + " to " + deckname);
            String response = is.readUTF();
            return response.matches(ServerStrings.CARD_ADDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public VBox requestLeaderBoard() throws IOException {
        os.writeUTF(ServerStrings.GET_LEADERBOARD);
        String response = is.readUTF();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        while (!response.equals("end")) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(LoginPageController.getInstance().makeMainLabel(response, 17));
            String status = is.readUTF();
            if (status.matches("Online")) {
                Label label = new Label(status);
                label.setFont(Font.font(17));
                label.setTextFill(Color.GREEN);
                hBox.getChildren().add(label);
            } else {
                Label label = new Label(status);
                label.setFont(Font.font(17));
                label.setTextFill(Color.RED);
                hBox.getChildren().add(label);
            }
            vBox.getChildren().add(hBox);
            response = is.readUTF();
        }
        return vBox;
    }

    public void logout() throws IOException {
        os.writeUTF(ServerStrings.LOGOUT);
        authToken = -1;
    }

    public boolean requestRemoveDeck(String deckName){
        try {
            os.writeUTF("remove deck:" + deckName);
            return is.readUTF().matches(ServerStrings.DECK_DELETED);
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean requestNewDeck(String deckName){
        try {
            os.writeUTF("new deck:" + deckName);
            return is.readUTF().matches(ServerStrings.NEW_DECK_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean requestSignUp(String username, String password) throws IOException {
        os.writeUTF("signup username:" + username + " password:" + password);
        String response = is.readUTF();
        return response.matches(ServerStrings.SIGNUP_SUCCESSFUL);
    }

    public boolean requestCardDeletion(Card card) {
        try {
            os.writeUTF("remove card:" + card.getId() + " from deck:" + Account.getEditingDeck().getDeckName());
            String res = is.readUTF();
            System.out.println(res);
            return res.matches(ServerStrings.CARD_DELETED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setAsMainDeck(Deck editingDeck) {
        try {
            os.writeUTF("set " + editingDeck.getDeckName() + " as main");
            return is.readUTF().matches(ServerStrings.MAIN_DECK_SET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
