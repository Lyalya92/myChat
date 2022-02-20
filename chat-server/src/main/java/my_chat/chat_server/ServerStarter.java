package my_chat.chat_server;

import my_chat.chat_server.authorization.InMemoryAuthService;
import my_chat.chat_server.authorization.DatabaseAuthService;
import my_chat.chat_server.server.Server;


public class ServerStarter {
    public static void main(String[] args) {

        Server server = new Server(new DatabaseAuthService());
        server.start();
    }
}
