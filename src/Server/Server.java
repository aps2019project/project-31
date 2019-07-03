package Server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.Account;

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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir") + "/Sources/ServerResources/config.txt"));
            port = Integer.parseInt(bufferedReader.readLine());
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> {
                    try {
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        while (true) {

                            String command = inputStream.readUTF();
                            Pattern pattern = Pattern.compile(ServerStrings.LOGIN);
                            Matcher matcher = pattern.matcher(command);
                            if (matcher.matches())
                            {
                                String username = matcher.group(1);
                                String password = matcher.group(2);
                                Account account = Account.findAccount(username);
                                if (account == null ||
                                    !account.getPassword().equals(password)){
                                    outputStream.writeUTF(ServerStrings.LOGINERROR);
                                    continue;
                                }
                                outputStream.writeUTF(ServerStrings.LOGINSUCCESS);
                                YaGson yaGson = new YaGsonBuilder().create();
                                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                                byte[] bytes = yaGson.toJson(account).getBytes();
                                out.writeInt(bytes.length);
                                for (byte b: bytes){
                                    out.writeByte(b);
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
