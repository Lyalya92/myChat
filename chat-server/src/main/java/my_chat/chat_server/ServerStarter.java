package my_chat.chat_server;

import my_chat.chat_server.server.Server;
import my_chat.chat_server.authorization.InMemoryAuthService;

public class ServerStarter {
    public static void main(String[] args) {

        Server server = new Server(new InMemoryAuthService());
        server.start();
    }
}
