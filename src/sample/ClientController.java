package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController {

    private ClientModel model;
    private boolean isRun = true;

    @FXML
    private TextField localhost;

    @FXML
    private TextField port;

    @FXML
    private TextField username;

    @FXML
    private Button inButton;

    @FXML
    private Button outButton;

    @FXML
    private Button online;

    @FXML
    private TextArea chatField;

    @FXML
    private TextField textField;

    @FXML
    void inPress(ActionEvent event) {
        model = new ClientModel();
        try {
            model.socket = new Socket(localhost.getText().trim(), Integer.parseInt(port.getText().trim()));
        } catch (IOException e) {
            appendMsg("Did not connect to server. Server don't work");
            return;
        }
        appendMsg("You are connect to the server successfully!");
        try {
            model.inPut = new ObjectInputStream(model.socket.getInputStream());
            model.outPut = new ObjectOutputStream(model.socket.getOutputStream());
        } catch (IOException e) {
            appendMsg("Exception trying connect to server socket" + e);
        }
        if (model.isConnected()) {
            youPressedIn();
            model.sendMessage(new Message(username.getText(), Message.MESSAGE));
        }
    }

    @FXML
    void onlinePress(ActionEvent event) {
        model.sendMessage(new Message(textField.getText(), Message.WHOISONLINE));
    }

    @FXML
    void outPress(ActionEvent event) {
        youPressedOut();
    }

    @FXML
    void enterMessage(ActionEvent event) {
        model.sendMessage(new Message(textField.getText(), Message.MESSAGE));
        textField.setText("");
    }

    private void appendMsg(String msg) {
        chatField.appendText(msg + "\n");
    }

    private void listenFromServer() {
        new Thread(() -> {
            String msg;
            while (isRun) {
                try {
                    msg = (String) model.getInPut().readObject();
                } catch (Exception e) {
                    appendMsg("Соединение с сервером разорвано.");
                    youPressedOut();
                    Thread.currentThread().interrupt();
                    break;
                }
                appendMsg(msg);
            }
        }).start();
    }

    /**
    set value for view components when you press OUT
     */
    public void youPressedOut() {
        model.sendMessage(new Message(textField.getText(), Message.EXIT));
        textField.setEditable(false);
        inButton.setDisable(false);
        outButton.setDisable(true);
        online.setDisable(true);
        localhost.setEditable(true);
        port.setEditable(true);
        username.setEditable(true);
        isRun = false;
    }

    /**
    set value for view components when you press in and start listen from server
     */
    public void youPressedIn() {
        textField.setEditable(true);
        textField.setText("");
        inButton.setDisable(true);
        outButton.setDisable(false);
        online.setDisable(false);
        localhost.setDisable(false);
        port.setDisable(false);
        username.setDisable(false);
        listenFromServer();
    }
}
