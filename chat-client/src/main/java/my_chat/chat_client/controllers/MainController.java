package my_chat.chat_client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import my_chat.chat_client.network.MessageProcessor;
import my_chat.chat_client.network.NetworkService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable, MessageProcessor {
    public static final String REGEX = "%!%";

    private  String nickname;
//    MainController mainController = (MainController) ControllerHandler.controllers.get("mainChatWindow");

    @FXML
    public TextArea mainChatArea;

    @FXML
    public ListView contactList;

    @FXML
    public TextField inputField;

    @FXML
    public Button btnSend;

    public void mockAction(ActionEvent actionEvent) {
    }

    public void sendMessage(ActionEvent actionEvent) {
        var message = inputField.getText();
        if (message.isBlank()) {
            return;
        }
        String recipient = (String) contactList.getSelectionModel().getSelectedItems().get(0);
        String messageType;
        String outMessage;
        if (recipient.equals("ALL")) {
            messageType = "/broadcast";
            outMessage = messageType + REGEX + nickname + REGEX + message;
        } else {
            messageType = "/private";
            outMessage = messageType + REGEX + recipient + REGEX + message;
        }
        AuthController authController = (AuthController) ControllerHandler.controllers.get("authWindow");
        authController.networkService.sendMessage(outMessage);
        inputField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void processMessage(String message) {
        Platform.runLater(()->parseIncomingMessage(message));
    }

    private void parseIncomingMessage(String message) {
        var splitMessage = message.split(REGEX);
        switch (splitMessage[0]) {
            case "/auth_ok" :
                this.nickname = splitMessage[1];
                closeAuthWindow();
                showMainChatWindow(splitMessage[1]);
                break;
            case "/error" :
                showError(splitMessage[1]);
                System.out.println("got error " + splitMessage[1]);
                break;
            case "/list" :
                addContactList(splitMessage);
                break;
            default:
                addMessageToChatArea(splitMessage[0]);
                break;
        }
    }


    private void showError (String message){
        var alert = new Alert(Alert.AlertType.ERROR,
                "An error occured: " + message,
                ButtonType.OK);
        alert.showAndWait();
    }

    private void showMainChatWindow(String nickname) {
        MainController mainController = (MainController) ControllerHandler.controllers.get("mainChatWindow");
        Stage stage = (Stage) mainController.btnSend.getScene().getWindow();
        var title = stage.getTitle() + " [" + nickname + "]";
        stage.setTitle(title);
        stage.showAndWait();
    }

    private void closeAuthWindow() {
        AuthController controller = (AuthController) ControllerHandler.controllers.get("authWindow");
        controller.closeWindow();
    }

    private void addContactList(String [] splitMessage){

        var contacts = new ArrayList<String>();
        contacts.add("ALL");
        for (int i = 1; i < splitMessage.length; i++) {
            contacts.add(splitMessage[i]);
        }
        MainController mainController = (MainController) ControllerHandler.controllers.get("mainChatWindow");
        mainController.contactList.setItems(FXCollections.observableList(contacts));
        mainController.contactList.getSelectionModel().selectFirst();


    }

    private void addMessageToChatArea(String message) {
        mainChatArea.appendText(message + System.lineSeparator());
    }
}
