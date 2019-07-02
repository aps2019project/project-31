package Server;

import model.Account;

import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;

public class User extends Thread{
    private static ArrayList<User> users = new ArrayList<>();
    private Socket socket;
    private int authToken = -1;
    private Account account;

    public User(Socket socket, Account account) {
        this.socket = socket;
        this.account = account;
        users.add(this);
        authToken = generateAuthToken();
    }

    private int generateAuthToken() {
        return Math.abs(account.getUsername().hashCode());
    }
}
