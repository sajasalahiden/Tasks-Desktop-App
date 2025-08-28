package app.controller;

import app.config.ServiceLocator;
import app.model.User;
import app.repo.UserRepository;
import app.security.Hashing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;

public class loginPageController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passFeild;

    private final UserRepository users = ServiceLocator.users();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = safeTrim(emailField.getText());
        String password = safeTrim(passFeild.getText());

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in both fields.");
            return;
        }

        try {
            Optional<User> uOpt = users.findByEmail(email);
            if (uOpt.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "User not found.");
                return;
            }

            User u = uOpt.get();

            String givenHash = Hashing.sha256(password);

            String storedHash = u.getPasswordHash();

            if (storedHash == null || !storedHash.equals(givenHash)) {
                showAlert(Alert.AlertType.ERROR, "Incorrect email or password.");
                return;
            }

            goToHelloPage(event, u);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login failed: " + ex.getMessage());
        }
    }

    @FXML
    private void goToSignupPage(ActionEvent event) {
        try {
            Parent signupRoot = FXMLLoader.load(getClass().getResource("/app/view/signupPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to open signup page.");
        }
    }

    private void goToHelloPage(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/helloPage.fxml"));
            Parent root = loader.load();

            helloPageController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to open hello page.");
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
