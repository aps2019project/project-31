package model;

import Server.ServerStrings;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.GameMode;
import controller.BattlePageController;
import controller.LoginPageController;
import controller.Shop;

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

            Account account = (Account) receiveObject(yaGson, is);
            int auth = Integer.parseInt(is.readUTF());
            setAuthToken(auth);
            HashMap<Integer, Integer> stock = yaGson.fromJson(is.readUTF(), HashMap.class);
            Shop.setStock(stock);
            return account;
        } else return null;
    }

    public static Object receiveObject(YaGson yaGson, DataInputStream is) throws IOException {
        int size = Integer.parseInt(is.readUTF());
        byte[] bytes = new byte[size];

        for (int i = 0; i < size; i++) {
            byte b = is.readByte();
            bytes[i] = b;
        }
        String accountString = new String(bytes);
        return yaGson.fromJson(accountString, Account.class);

    }

    public void sendPlayRequest(GameMode gameMode) {
        try {
            switch (gameMode) {
                case DeathMatch:
                    os.writeUTF("DeathMatch request from user:" + Account.getMainAccount().getUsername());
                    break;
                case Flag:
                    os.writeUTF("Flag request from user:" + Account.getMainAccount().getUsername());
                    break;
                case Domination:
                    os.writeUTF("Domination request from user:" + Account.getMainAccount().getUsername());
                    break;
            }
            String serverReply = is.readUTF();
            if (serverReply.equals(ServerStrings.MULTIPLAYERSUCCESS)) {
                receiveBattleManager();
                BattlePageController.getInstance().setAsScene();
            } else {
                // some pop up happens !


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveBattleManager() {

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
}
