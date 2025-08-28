package app.controller;

import app.config.ServiceLocator;
import app.model.User;
import app.repo.UserRepository;
import app.security.Hashing;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class signupPageController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField2;
    @FXML
    private TextField passFeild;

    private final UserRepository users = ServiceLocator.users();

    @FXML
    private void handleSignup(javafx.event.ActionEvent event) {
        String fullName = safeTrim(nameField.getText());
        String email = safeTrim(emailField2.getText());
        String password = safeTrim(passFeild.getText());

        StringBuilder errors = new StringBuilder();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errors.append("- Please fill in all fields.\n");
        }

        if (!fullName.matches(".*[a-zA-Zأ-ي].*")) {
            errors.append("- Name must contain letters.\n");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.append("- Invalid email format.\n");
        }

        if (password.length() < 7) {
            errors.append("- Password must be at least 7 characters long.\n");
        }

        try {
            if (users.findByEmail(email).isPresent()) {
                errors.append("- Email already registered.\n");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to check email: " + e.getMessage());
            return;
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return;
        }

        try {
            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPasswordHash(Hashing.sha256(password));

            users.save(newUser);

            showAlert(Alert.AlertType.INFORMATION, "Account created successfully!");
            goToLoginPage(event);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to create account: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin(javafx.event.ActionEvent event) {
        goToLoginPage(event);
    }

    private void goToLoginPage(javafx.event.ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/app/view/loginPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load login page.");
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
