package model;

import Server.ServerStrings;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.AbstractCollection;

public class Client extends Thread {
    private Socket socket;
    private static Client client;

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
    }

    public Account attemptLogin(String username, String password) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("login username:" + username +
                " password:" + password);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String command = dataInputStream.readUTF();
        if (command.matches(ServerStrings.LOGINSUCCESS)) {
            System.out.println("getting account...");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            int size = Integer.parseInt(dataInputStream.readUTF());
            byte[] bytes = new byte[size];
            bytes = dataInputStream.readNBytes(size);
            /*for (int i = 0; i < size; i++) {
                byte b = in.readByte();

                System.out.println(i + ": received byte " + b);
                bytes[i] = b;

            }*/
            String s = new String(bytes);
            YaGson yaGson = new YaGsonBuilder().create();
            return yaGson.fromJson(s, Account.class);
        } else return null;

    }
}
