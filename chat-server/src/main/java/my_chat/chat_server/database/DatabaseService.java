package my_chat.chat_server.database;

import my_chat.chat_server.error.UserNotFoundException;
import my_chat.chat_server.server.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {
    public static final String CREATE_DB =
            "CREATE TABLE IF NOT EXISTS chat_clients (" +
             "id integer PRIMARY KEY AUTOINCREMENT," +
             "login TEXT NOT NULL UNIQUE," +
             "password TEXT NOT NULL," +
             "nickname TEXT NOT NULL UNIQUE);";

    public static final String INIT_DB =
            "INSERT INTO chat_clients (login, password, nickname) VALUES" +
            "('login1', 'qwert', 'nick1')," +
            "('login2', 'qwert', 'nick2')," +
            "('login3', 'qwert', 'nick3');";

    public static final String GET_NICKNAME =
            "SELECT nickname FROM chat_clients " +
            "WHERE login = ? AND password = ?";

    public static final String CHANGE_NICKNAME =
            "UPDATE chat_clients SET nickname = ? WHERE login = ?;";


    private  static Connection connection;
    private static DatabaseService instance;

    private DatabaseService() {
        try {
            connect();
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        createDB();
    }

    private void createDB() {
        try (var stmt = connection.createStatement();){
            stmt.execute(CREATE_DB);
            stmt.execute(INIT_DB);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
        Server.logger.info("Connected to db");
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                Server.logger.info("Disconnected to db");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void close() {
        disconnect();
    }
    
    public static DatabaseService getInstance() {
        if (instance!=null) return instance;
        instance = new DatabaseService();
        return instance;
    }

    public String getNickByLoginAndPassword(String login, String password) {
        try(var ps = connection.prepareStatement(GET_NICKNAME)){
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                var result = rs.getString("nickname");
                rs.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException ("User not found");
    }

    public String changeNickname(String login, String newNickname) {
        System.out.println("Login: " + login + " new Nick: " + newNickname);
        System.out.println("Мы готовы сменить ник!");
        try(var ps = connection.prepareStatement(CHANGE_NICKNAME)){
            ps.setString(1, newNickname);
            ps.setString(2, login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException ("User not found");

    }
}
