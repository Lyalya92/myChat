package my_chat.chat_server.server;

import my_chat.chat_server.authorization.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final String REGEX = "%!%";
    private static final int PORT = 8189;
    private ExecutorService executorService;
    public static final Logger logger = LogManager.getLogger(Server.class);

    private AuthService authService;
    private List<ClientHandler> clients;

    private Socket socket;
    private ServerSocket serverSocket;

    // Конструктор
    public Server(AuthService authService) {
        this.authService = authService;
        this.clients = new ArrayList<ClientHandler>();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);  //Пробуем запустить сервер
            logger.info("Server started");
            authService.start();

            while (true) {
                socket = serverSocket.accept();  // ждем подключения
                logger.info("Client connected");
                ClientHandler client = new ClientHandler(socket, this);  // создаем обработчик клиента
                client.handle();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            authService.stop();
            shutdown();
        }
    }

    // Сервер отправляет сообщение всем контактам
    public void sendMessageToAll(String from, String message) {
        var outMessage = String.format("<%s>: %s",from, message);
        for(ClientHandler c : clients) {
            c.sendMessage(outMessage);
        }
    }

    // Приватное сообщение от одного контакта к другому
    public void sendPrivateMessage (String from, String recipient, String message, ClientHandler sender) {
        ClientHandler clientHandler = null;
        for(ClientHandler c : clients) {
            if (c.getUserNickName().equals(recipient)) {
                clientHandler = c;
            }
        }
        if (clientHandler == null) {return;}
        message = String.format("<%s> -> <%s>: %s", from,recipient,message);

        clientHandler.sendMessage(message);
        sender.sendMessage(message);
    }

    // Добавление контакта в список
    public synchronized void addAuthorizedClientToList(ClientHandler client){
        clients.add(client);
        sendOnlineClients();
    }
    // Удаление контакта из списка
    public synchronized void removeAuthorizedClientFromList(ClientHandler client){
        clients.remove(client);
        sendOnlineClients();
        logger.info("Client disconnected");
    }

    // Отправка списка контактов
    public void sendOnlineClients(){
        StringBuilder sb = new StringBuilder("/list");
        sb.append(REGEX);
        for (ClientHandler clientHandler: clients) {
            sb.append(clientHandler.getUserNickName());
            sb.append(REGEX);
        }
        var message = sb.toString();
        for (ClientHandler clientHandler: clients) {
            clientHandler.sendMessage(message);
        }
    }

    // Проверка занято ли имя
    public synchronized boolean isNicknameBusy(String nickname){
        for (ClientHandler clientHandler: clients) {
            if (clientHandler.getUserNickName().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    // Завершение
    public void shutdown() {
        authService.stop();
        executorService.shutdownNow();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}

