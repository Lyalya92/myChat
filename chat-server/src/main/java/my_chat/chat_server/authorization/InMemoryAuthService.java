package my_chat.chat_server.authorization;

import my_chat.chat_server.entity.User;
import my_chat.chat_server.error.WrongCredentialsException;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAuthService implements AuthService{

    private List<User> users;

    public InMemoryAuthService() {
        this.users = new ArrayList<>();
        users.addAll(List.of(
                new User("login1", "qwert", "nick1", "word"),
                new User("login2", "qwert", "nick2", "word"),
                new User("login3", "qwert", "nick3", "word"),
                new User("login4", "qwert", "nick4", "word"),
                new User("login5", "qwert", "nick5", "word")
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
        for (User user:users) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())){
                return user.getNickname();
            }
        }
        throw new WrongCredentialsException("Wrong username or password");
    }

    @Override
    public User createNewUser(String login, String password, String nickName) {
        return null;
    }

    @Override
    public void deleteUser(String login, String password) {

    }

    @Override
    public String changeNickname(String newNickname) {
        return null;
    }

    @Override
    public void changePassword(String login, String oldPass, String newPass) {

    }

    @Override
    public void resetPassword(String login, String newPass, String secretWord) {

    }
}
