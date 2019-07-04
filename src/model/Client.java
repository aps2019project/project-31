package model;

import Server.ServerStrings;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import controller.Shop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
            int size = Integer.parseInt(is.readUTF());
            byte[] bytes = new byte[size];

            for (int i = 0; i < size; i++) {
                byte b = is.readByte();
                bytes[i] = b;
            }
            String s = new String(bytes);
            YaGson yaGson = new YaGsonBuilder().create();
            int auth = Integer.parseInt(is.readUTF());
            setAuthToken(auth);
            HashMap<Integer, Integer> stock = yaGson.fromJson(is.readUTF(), HashMap.class);
            Shop.setStock(stock);
            return yaGson.fromJson(s, Account.class);
        } else return null;
    }

    public void sendConnectionRequest() {
        try {
            os.writeUTF("connection request from user:" );


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestCardStock(int id) {
        try {
            os.writeUTF("request card stock: " + id);
            return is.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
