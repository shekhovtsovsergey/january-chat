package ru.geekbrains.january_chat.chat_server.auth;

import ru.geekbrains.january_chat.chat_server.db.SqlClient;
import ru.geekbrains.january_chat.chat_server.entity.User;
import ru.geekbrains.january_chat.chat_server.error.WrongCredentialsException;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAuthService implements AuthService {

    private List<User> users;

    public InMemoryAuthService() {
        this.users = new ArrayList<>();
        users.addAll(List.of(
                new User("111", "111", "nick1", "secret"),
                new User("222", "222", "nick2", "secret"),
                new User("333", "333", "nick3", "secret"),
                new User("444", "444", "nick4", "secret"),
                new User("555", "555", "nick5", "secret")
        ));
    }

    @Override
    public void start() {
        System.out.println("Auth service started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }

    @Override
    public String authorizeUserByLoginAndPassword(String login, String password) {
    /*
        for (User user : users) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return user.getNick();
            }
        }
        throw new WrongCredentialsException("Wrong username or password");
    }
    */


        String nickname = SqlClient.getNick(login, password);
        if (nickname == null) {
            throw new WrongCredentialsException("Wrong username or password");
        }
        System.out.println("login = " + login + ", password = " + password);
        return nickname;

    }



    @Override
    public String changeNick(String login, String newNick) {
        return null;
    }

    @Override
    public User createNewUser(String login, String password, String nick) {
        return null;
    }

    @Override
    public void deleteUser(String login, String pass) {

    }

    @Override
    public void changePassword(String login, String oldPass, String newPass) {

    }

    @Override
    public void resetPassword(String login, String newPass, String secret) {

    }
}
