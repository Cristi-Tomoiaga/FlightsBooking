package map.project.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.project.MainApplication;
import map.project.domain.Client;
import map.project.service.Service;
import map.project.utils.MessageAlert;

import java.io.IOException;

public class LoginController {
    public TextField usernameField;
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public void handleLogin() {
        String username = usernameField.getText().trim();
        if ("".equals(username)) {
            MessageAlert.showErrorMessage(null, "Invalid username");
            usernameField.setText("");
            return;
        }

        Client client = service.login(username);
        if (client == null) {
            MessageAlert.showErrorMessage(null, "Invalid username");
            usernameField.setText("");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/flight-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            FlightController controller = fxmlLoader.getController();
            controller.setServiceClient(service, client);
            Stage stage = new Stage();
            stage.setTitle("Client - " + client.getName());
            stage.setScene(scene);
            stage.show();

            usernameField.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
