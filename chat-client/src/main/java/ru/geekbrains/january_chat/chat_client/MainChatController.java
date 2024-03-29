package ru.geekbrains.january_chat.chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.geekbrains.january_chat.chat_client.network.MessageProcessor;
import ru.geekbrains.january_chat.chat_client.network.NetworkService;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {
    public static final String REGEX = "%!%";

    private String nick;
    private NetworkService networkService;

    @FXML
    private VBox changeNickPanel;

    @FXML
    private TextField newNickField;

    @FXML
    private VBox changePasswordPanel;

    @FXML
    private PasswordField oldPassField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private VBox loginPanel;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox mainChatPanel;

    @FXML
    private TextArea mainChatArea;

    @FXML
    private ListView contactList;

    @FXML
    private TextField inputField;

    @FXML
    private Button btnSend;

    public void connectToServer(ActionEvent actionEvent) throws IOException {
    }

    public void disconnectFromServer(ActionEvent actionEvent) {
    }

    public void mockAction(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void showHelp(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    public void sendMessage(ActionEvent actionEvent) {
        var message = inputField.getText();
        if (message.isBlank()) {
            return;
        }
        var recipient = contactList.getSelectionModel().getSelectedItem();
        if (!recipient.equals("ALL")) {
            networkService.sendMessage("/w" + REGEX + recipient + REGEX + message);
        } else {
            networkService.sendMessage("/broadcast" + REGEX + message);
        }
        inputField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.networkService = new NetworkService(this);
    }

    @Override
    public void processMessage(String message) {
        Platform.runLater(() -> {
            try {
                parseIncomingMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void parseIncomingMessage(String message) throws IOException {
        var splitMessage = message.split(REGEX);
        String login = this.nick;
        String filename = "history_" + login + ".txt";
        String filenameGeneral = "history_.txt";
        switch (splitMessage[0]) {
            case "/auth_ok":
                this.nick = splitMessage[1];
                loginPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                loadHistory();
                break;
            case "/error":
                showError(splitMessage[1]);
                System.out.println("got error " + splitMessage[1]);
                break;
            case "/list":
                var contacts = new ArrayList<String>();
                contacts.add("ALL");
                for (int i = 1; i < splitMessage.length; i++) {
                    contacts.add(splitMessage[i]);
                }
                contactList.setItems(FXCollections.observableList(contacts));
                contactList.getSelectionModel().selectFirst();
                break;
            case "/change_pass_ok":
                changePasswordPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                break;
            default:
                mainChatArea.appendText(splitMessage[0] + System.lineSeparator());
                writeFileLog(filename, message);
                writeFileLog(filenameGeneral, message);
                break;
        }
    }

    public void sendChangeNick(ActionEvent actionEvent) {
        if (newNickField.getText().isBlank()) return;
        networkService.sendMessage("/change_nick" + REGEX + newNickField.getText());
    }

    public void sendChangePass(ActionEvent actionEvent) {
        if (newPasswordField.getText().isBlank() || oldPassField.getText().isBlank()) return;
        networkService.sendMessage("/change_pass" + REGEX + oldPassField.getText() + REGEX + newPasswordField.getText());
    }

    public void sendEternalLogout(ActionEvent actionEvent) {
        networkService.sendMessage("/remove");
    }

    private void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR,
                "An error occured: " + message,
                ButtonType.OK);
        alert.showAndWait();
    }

    public void sendAuth(ActionEvent actionEvent) {
        var login = loginField.getText();
        var password = passwordField.getText();
        if (login.isBlank() || password.isBlank()) {
            return;
        }

        var message = "/auth" + REGEX + login + REGEX + password;

        if (!networkService.isConnected()) {
            try {
                networkService.connect();
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());

            }
        }

        networkService.sendMessage(message);
    }

    public void returnToChat(ActionEvent actionEvent) throws IOException {
        changeNickPanel.setVisible(false);
        changePasswordPanel.setVisible(false);
        mainChatPanel.setVisible(true);
    }

    public void showChangeNick(ActionEvent actionEvent) {
        mainChatPanel.setVisible(false);
        changeNickPanel.setVisible(true);
    }

    public void showChangePass(ActionEvent actionEvent) {
        mainChatPanel.setVisible(false);
        changePasswordPanel.setVisible(true);
    }




    private static void writeFileLog(String filename, String message) throws IOException {

        File file2 = new File(filename);

        if (file2.exists()) {
            Writer writer = new FileWriter(file2, true);
            String line = message;
            System.out.println(line);
            writer.append(line + "\n");
            writer.flush();
        } else {
            file2.createNewFile();
            Writer writer = new FileWriter(file2);
            String line = message;
            System.out.println(line);
            writer.append(line + "\n");
            writer.flush();
        }
    }



    private void loadHistory() throws IOException {

        int posHistory = 100;
        File history = new File("history_.txt");
        List<String> historyList = new ArrayList<>();
        FileInputStream in = new FileInputStream(history);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            historyList.add(temp);
        }

        if (historyList.size() > posHistory) {
            for (int i = historyList.size() - posHistory; i <= (historyList.size() - 1); i++) {
                mainChatArea.appendText(historyList.get(i) + "\n");
            }
        } else {
            for (int i = 0; i < posHistory; i++) {
                mainChatArea.appendText(historyList.get(i) + "\n");
            }
        }
    }



}



