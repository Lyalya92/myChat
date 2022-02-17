package my_chat.chat_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_chat.chat_client.controllers.ControllerHandler;

public class ChatApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage authStage) throws Exception {

        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(getClass().getResource("/authWindow.fxml"));
        Parent authParent = authLoader.load();
        ControllerHandler.controllers.put("authWindow", authLoader.getController());
        Scene authScene = new Scene(authParent);
        authStage.setScene(authScene);
        authStage.setTitle("Authorization");
        authStage.show();

        FXMLLoader mainChatLoader = new FXMLLoader();
        mainChatLoader.setLocation(getClass().getResource("/mainChatWindow.fxml"));
        Parent parent = mainChatLoader.load();
        ControllerHandler.controllers.put("mainChatWindow", mainChatLoader.getController());
        Stage mainChatStage = new Stage();
        mainChatStage.setScene(new Scene(parent));
        mainChatStage.setTitle("MyChat");



    }
}
