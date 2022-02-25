package my_chat.chat_server.server;

import my_chat.chat_server.error.WrongCredentialsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {
    private final long authTimeout = 60000;
    private String user;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private Thread handlerTread;


    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        // создание нового потока для обработки
        handlerTread = new Thread(()-> {
            authorize();
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                try {
                    var message = in.readUTF();
                    handleMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerTread.start();
    }

    // Обработка сообщения:
    private void handleMessage(String message) {
        var splitMessage = message.split(Server.REGEX);
        switch (splitMessage[0]) {
            case "/private":
                server.sendPrivateMessage(this.user, splitMessage[1], splitMessage[2],this);
                break;
            case "/broadcast":
                server.sendMessageToAll(this.user, splitMessage[2]);
                break;
            case "/change_nick":
                if (server.getAuthService().authorizeUserByLoginAndPassword(splitMessage[1], splitMessage[2])!=null) {
                    server.getAuthService().changeNickname(splitMessage[1], splitMessage[3]);
                }
                break;
            case "/disconnect":
                clientDisconnect();
                break;
        }

    }

    // Авторизация:
    private void authorize(){
        System.out.println("Authorizing");
        var timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (user == null) {
                        sendMessage("/error" + Server.REGEX + "Authentication timeout!\nPlease, try again later!");
                        Thread.sleep(50);
                        socket.close();
                        System.out.println("Connection with client closed");
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, authTimeout);

        try {
            while (true) {
                var message = in.readUTF();
                if (message.startsWith("/auth")) {
                    var parsedAuthMessage = message.split(Server.REGEX);
                    var response = "";
                    String nickname = null;
                    try {
                        nickname = server.getAuthService().authorizeUserByLoginAndPassword(parsedAuthMessage[1], parsedAuthMessage[2]);
                    } catch (WrongCredentialsException e) {
                        response = "/error" + Server.REGEX + e.getMessage();
                        System.out.println("Wrong login or password");
                    }

                    if (server.isNicknameBusy(nickname)) {
                        response = "/error" + Server.REGEX + "this client already connected";
                        System.out.println("Nickname busy " + nickname);
                    }

                    if (!response.equals("")) {
                        sendMessage(response);
                    } else {
                        this.user = nickname;
                        sendMessage("/auth_ok" + Server.REGEX + nickname);
                        server.addAuthorizedClientToList(this);
                        break;
                         }
                     }
                 }
            }  catch(IOException e){
                e.printStackTrace();
            }


    }

    // Метод, отправляющий сообщение
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Клиент отключается
    public void clientDisconnect() {
        server.removeAuthorizedClientFromList(this);
        handlerTread.interrupt();
        System.out.println("Client disconnected");
    }


    public String getUserNickName() {
        return this.user;
    }

}
