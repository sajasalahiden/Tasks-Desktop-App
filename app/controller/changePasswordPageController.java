package app.controller;

import app.config.ServiceLocator;
import app.model.User;
import app.repo.UserRepository;
import app.security.Hashing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class changePasswordPageController implements helloPageController.RequiresUser {

    @FXML
    private TextField oldTf;
    @FXML
    private TextField newTf;
    @FXML
    private TextField confirmTf;

    private final UserRepository users = ServiceLocator.users();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void saveAction() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "No active user. Please log in again.");
            return;
        }

        String oldPass = safeTrim(oldTf.getText());
        String newPass = safeTrim(newTf.getText());
        String confirm = safeTrim(confirmTf.getText());

        if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        String oldHash = Hashing.sha256(oldPass);
        if (!oldHash.equals(currentUser.getPasswordHash())) {
            showAlert(Alert.AlertType.ERROR, "Old password is incorrect.");
            return;
        }

        if (!newPass.equals(confirm)) {
            showAlert(Alert.AlertType.ERROR, "New password and confirmation do not match.");
            return;
        }

        if (newPass.length() < 7) {
            showAlert(Alert.AlertType.ERROR, "Password must be at least 7 characters long.");
            return;
        }

        try {
            String newHash = Hashing.sha256(newPass);

            if (currentUser.getId() == null) {
                var uOpt = users.findByEmail(currentUser.getEmail());
                if (uOpt.isPresent()) {
                    currentUser = uOpt.get();
                } else {
                    showAlert(Alert.AlertType.ERROR, "User not found. Please log in again.");
                    return;
                }
            }

            users.updatePasswordById(currentUser.getId(), newHash);

            currentUser.setPasswordHash(newHash);

            showAlert(Alert.AlertType.INFORMATION, "Password updated successfully. Please log in again.");
            goToLoginPage();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to update password: " + e.getMessage());
        }
    }

    private void goToLoginPage() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/app/view/loginPage.fxml"));
            Stage stage = (Stage) oldTf.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load login page.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Change Password");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
