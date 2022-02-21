package my_chat.chat_client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import my_chat.chat_client.network.MessageProcessor;
import my_chat.chat_client.network.NetworkService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable, MessageProcessor {
    public static final String REGEX = "%!%";
    public NetworkService networkService;

    @FXML
    public TextField loginField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button btnSignIn;

    @FXML
    public Button btnCreateAnAccount;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.networkService = new NetworkService(this);
    }

    public void sendAuth(ActionEvent actionEvent) {
        var login = loginField.getText();
        var password = passwordField.getText();
        if (login.isBlank() || password.isBlank()) {
            return;
        }

        var message = "/auth" + REGEX + login + REGEX + password;
        if (!networkService.isConnected()) {
            try {
                networkService.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        networkService.sendMessage(message);

    }


    public void createAnAccount(ActionEvent actionEvent) {
    }

    @Override
    public void processMessage(String message) {
        MainController mainController = (MainController) ControllerHandler.controllers.get("mainChatWindow");
        mainController.processMessage(message);
    }

    public void closeWindow() {
        Stage stage = (Stage) btnSignIn.getScene().getWindow();
        stage.hide();
    }

}
