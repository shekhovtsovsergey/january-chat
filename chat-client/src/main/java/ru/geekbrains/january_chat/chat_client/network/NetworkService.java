package ru.geekbrains.january_chat.chat_client.network;

import ru.geekbrains.january_chat.props.PropertyReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkService {
    private final String host;
    private final int port;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private MessageProcessor messageProcessor;

    public NetworkService(MessageProcessor messageProcessor) {
        host = PropertyReader.getInstance().getHost();
        port = PropertyReader.getInstance().getPort();
        this.messageProcessor = messageProcessor;
    }

    public void connect() throws IOException {
        this.socket = new Socket(host, port);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        readMessages();
    }

    public void readMessages() {
        var thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    var message = in.readUTF();
                    messageProcessor.processMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
