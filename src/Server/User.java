package Server;

import controller.Shop;
import model.Account;
import model.Card;

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


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shopRequestStockHandler(String command) throws IOException {

        Pattern pattern = Pattern.compile(ServerStrings.REQUEST_STOCK);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            Card card = Shop.findCardById(Integer.parseInt(matcher.group(1)));
            if (card == null) {
                dataOutputStream.writeUTF("Invalid card ID!");
            } else {
                dataOutputStream.writeUTF(card.getName() +
                        ": " +
                        Shop.getStock().get(card.getId()));
            }
        }
    }
    private void multiPlayerRequestHandler(){

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
