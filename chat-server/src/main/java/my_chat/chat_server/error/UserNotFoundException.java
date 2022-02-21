package my_chat.chat_server.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
