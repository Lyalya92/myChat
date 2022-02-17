package my_chat.chat_server.entity;

public class User {
    private String login;
    private String password;
    private String nickname;
    String secretWord;

    public User(String login, String password, String nickname, String secretWord) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
        this.secretWord = secretWord;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }
}
