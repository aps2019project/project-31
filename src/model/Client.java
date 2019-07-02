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

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Account attemptLogin(String username, String password) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("login username:" + username +
                " login:" + password);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String command = dataInputStream.readUTF();
        if (command.matches(ServerStrings.LOGINSUCCESS)) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            int size = in.readInt();
            byte[] bytes = new byte[size];
            for (int i = 0; i < size; i++) {
                bytes[i] = in.readByte();
            }
            String s = new String(bytes);
            YaGson yaGson = new YaGsonBuilder().create();
            return yaGson.fromJson(s, Account.class);
        } else return null;

    }
}
