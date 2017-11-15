package sample;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientModel {

    ObjectOutputStream outPut;
    ObjectInputStream inPut;
    Socket socket;

    public ClientModel() {
    }

    public void display(String msg) {
        System.out.println(msg);
    }

    public void sendMessage(Message msg) {
        try {
            outPut.writeObject(msg);
            outPut.flush();
        } catch (IOException e) {
            display("Exception writing to server " + e);
        }
    }

    public void disconnect() {
        try {
            if (inPut != null) {
                inPut.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии входного потока клиента");
            e.printStackTrace();
        }
        try {
            if (outPut != null) {
                outPut.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии выходного потока клиента");
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии сокета клиента");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public ObjectInputStream getInPut() {
        return inPut;
    }
}

