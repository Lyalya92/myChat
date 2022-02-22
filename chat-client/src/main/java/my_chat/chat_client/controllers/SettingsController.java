package my_chat.chat_client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import my_chat.chat_client.network.MessageProcessor;
import my_chat.chat_client.network.NetworkService;

import java.io.IOException;

public class SettingsController implements MessageProcessor {
    public static final String REGEX = "%!%";
    public NetworkService networkService;

    @FXML
    public VBox settingsPanel;

    @FXML
    public VBox changeNickPanel;

    @FXML
    public Button btnChangePassword;

    @FXML
    public Button btnChangeNickname;

    @FXML
    public TextField loginField;

    @FXML
    public TextField passwordField;

    @FXML
    public TextField newNicknameField;


    public void openChangeNicknamePanel(ActionEvent actionEvent) {
        settingsPanel.setVisible(false);
        changeNickPanel.setVisible(true);
    }

    public void backToSettings(ActionEvent actionEvent) {
        changeNickPanel.setVisible(false);
        settingsPanel.setVisible(true);
    }

    public void changeNickname(ActionEvent actionEvent) {
        var login = loginField.getText();
        var password = passwordField.getText();
        var newNick = newNicknameField.getText();
        if (login.isBlank() || password.isBlank()) {
            return;
        }

        var outMessage = "/change_nick" + REGEX + login + REGEX + password + REGEX + newNick;
        AuthController authController = (AuthController) ControllerHandler.controllers.get("authWindow");
        authController.networkService.sendMessage(outMessage);


    }

    @Override
    public void processMessage(String message) {
        MainController mainController = (MainController) ControllerHandler.controllers.get("mainChatWindow");
        mainController.processMessage(message);
    }
}
