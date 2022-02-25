package my_chat.chat_client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import my_chat.chat_client.controllers.ControllerHandler;
import my_chat.chat_client.controllers.MainController;

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
        MainController mainController = mainChatLoader.getController();
        ControllerHandler.controllers.put("mainChatWindow", mainController);
        Stage mainChatStage = new Stage();
        mainChatStage.setScene(new Scene(parent));
        mainChatStage.setTitle("MyChat");
        mainChatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                mainController.processMessage("/disconnect");
            }
        });


    }
}
