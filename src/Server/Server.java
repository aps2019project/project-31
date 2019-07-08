package Server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import controller.Shop;
import model.Account;
import model.Card;
import model.Initializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server extends Thread {
    private static ServerSocket server;
    private static int port;
    private static ArrayList<User> usersInChat = new ArrayList<>();

    public static ArrayList<User> getUsersInChat() {
        return usersInChat;
    }

    static void main(String[] args) {

        try {
            YaGson yaGson = new YaGsonBuilder().create();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Sources/ServerResources/config.txt"));
            port = Integer.parseInt(bufferedReader.readLine());

            bufferedReader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Sources/ServerResources/serverData.txt"));
            /*for (Card card: Shop.getAllCards()){
                Shop.getStock().put(card.getId(), 5);
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") +
                    "/Sources/ServerResources/serverData.txt"));
            bufferedWriter.write(yaGson.toJson(Shop.getStock()));
            bufferedWriter.flush();*/
            Shop.setStock(yaGson.fromJson(bufferedReader.readLine(), HashMap.class));

            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client " + socket.getLocalSocketAddress() + "connected");
                makeWaitForLoginThread(yaGson, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void saveAllAccounts() {
        Account.saveAllAccounts();
    }

    public static void makeWaitForLoginThread(YaGson yaGson, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    String command = inputStream.readUTF();
                    System.out.println(command + " received");
                    Pattern pattern = Pattern.compile(ServerStrings.LOGIN);
                    Matcher matcher = pattern.matcher(command);
                    if (matcher.matches()) {
                        String username = matcher.group(1);
                        System.out.println("client logging in to account " + username);
                        String password = matcher.group(2);
                        Account account = Account.findAccount(username);
                        if (account == null ||
                                !account.getPassword().equals(password)) {
                            outputStream.writeUTF(ServerStrings.LOGINERROR);
                            System.out.println(account);
                            continue;
                        }
                        outputStream.writeUTF(ServerStrings.LOGINSUCCESS);
                        outputStream.flush();
                        System.out.println(ServerStrings.LOGINSUCCESS);
                        User user = new User(socket, account);
                        sendObject(account, outputStream);
                        /*outputStream.writeUTF(bytes.length + "");
                        outputStream.flush();

                        System.out.println("sending account with " + bytes.length);
                        for (int i = 0; i < bytes.length; i++) {
                            outputStream.writeByte(bytes[i]);
                        }
*/
                        System.out.println("Sending auth token");
                        outputStream.writeUTF(user.getAuthToken() + "");
                        System.out.println("sending shop stock");
                        outputStream.writeUTF(yaGson.toJson(Shop.getStock()));
                        user.start();
                        break;

                    }


                    pattern = Pattern.compile(ServerStrings.REQUEST_SIGNUP);
                    matcher = pattern.matcher(command);
                    if (matcher.matches()) {
                        String username = matcher.group(1);
                        String password = matcher.group(2);
                        if (Account.findAccount(username) != null) {
                            outputStream.writeUTF(ServerStrings.ALREADY_TAKEN);
                            continue;
                        }
                        Account.createAccount(username, password.trim());
                        saveAllAccounts();
                        outputStream.writeUTF(ServerStrings.SIGNUP_SUCCESSFUL);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void sendObject(Object object, DataOutputStream os) throws IOException {
        YaGson yaGson = new YaGsonBuilder().create();
        byte[] objectBytes = yaGson.toJson(object).getBytes();
        os.writeUTF(objectBytes.length + "");
        os.flush();
        for (int i = 0; i < objectBytes.length; i++) {
            os.writeByte(objectBytes[i]);
        }

    }
}
