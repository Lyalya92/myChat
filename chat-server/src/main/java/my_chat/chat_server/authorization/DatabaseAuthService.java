package my_chat.chat_server.authorization;

import my_chat.chat_server.database.DatabaseService;
import my_chat.chat_server.entity.User;

public class DatabaseAuthService implements AuthService {
    private DatabaseService databaseService;

    @Override
    public void start() {
        databaseService = DatabaseService.getInstance();
    }

    @Override
    public void stop() {
        databaseService.close();
    }

    @Override
    public String authorizeUserByLoginAndPassword(String login, String password) {
        return databaseService.getNickByLoginAndPassword(login, password);
    }

    @Override
    public User createNewUser(String login, String password, String nickName) {
        return null;
    }

    @Override
    public void deleteUser(String login, String password) {

    }

    @Override
    public String changeNickname(String login, String newNickname) {
        return databaseService.changeNickname(login, newNickname);
    }

    @Override
    public void changePassword(String login, String oldPass, String newPass) {

    }

    @Override
    public void resetPassword(String login, String newPass, String secretWord) {

    }
}
