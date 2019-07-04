package Server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.Account;
import model.Initializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server extends Thread {
    private static ServerSocket server;
    private static int port;

    public static void main(String[] args) {
        try {
            Initializer.initialiseData();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Sources/ServerResources/config.txt"));
            port = Integer.parseInt(bufferedReader.readLine());
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client " + socket.getLocalSocketAddress() + "connected");
                new Thread(() -> {
                    try {
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        while (true) {
                            String command = inputStream.readUTF();
                            System.out.println(command + " received");
                            Pattern pattern = Pattern.compile(ServerStrings.LOGIN);
                            Matcher matcher = pattern.matcher(command);
                            if (matcher.matches())
                            {
                                String username = matcher.group(1);
                                System.out.println("client logging in to account " + username);
                                String password = matcher.group(2);
                                Account account = Account.findAccount(username);
                                if (account == null ||
                                    !account.getPassword().equals(password)){
                                    outputStream.writeUTF(ServerStrings.LOGINERROR);
                                    System.out.println(account);
                                    continue;
                                }
                                outputStream.writeUTF(ServerStrings.LOGINSUCCESS);
                                outputStream.flush();
                                System.out.println(ServerStrings.LOGINSUCCESS);
                                YaGson yaGson = new YaGsonBuilder().create();
                                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                                byte[] bytes = yaGson.toJson(account).getBytes();
                                outputStream.writeUTF(bytes.length + "");
                                outputStream.flush();

                                System.out.println("sending account with " + bytes.length);
                                for (int i = 0; i < bytes.length; i++) {
                                    outputStream.writeByte(bytes[i]);

                                }
                                new User(socket,account).start();
                                break;

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
