package my_chat.chat_server.authorization;

import my_chat.chat_server.entity.User;

public interface AuthService {
    void start();
    void stop();

    String authorizeUserByLoginAndPassword (String login, String password);
    User createNewUser(String login, String password, String nickName);
    void deleteUser(String login, String password);
    String changeNickname(String login, String newNickname);
    void changePassword(String login, String oldPass, String newPass);
    void resetPassword(String login, String newPass, String secretWord);
}
