package ru.geekbrains.january_chat.chat_server;

import ru.geekbrains.january_chat.chat_server.auth.InMemoryAuthService;
import ru.geekbrains.january_chat.chat_server.server.Server;

import java.util.concurrent.ExecutorService;

public class App {
    private static ExecutorService executorService;

    public static void main(String[] args) {
        new Server(new InMemoryAuthService(), executorService).start();
    }
}
