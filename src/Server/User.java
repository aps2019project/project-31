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
