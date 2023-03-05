package map.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.project.controller.LoginController;
import map.project.service.Service;
import map.project.service.ServiceFactory;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Service service = ServiceFactory.getService();

        for (int i = 0; i < 4; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 250, 250);
            LoginController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}