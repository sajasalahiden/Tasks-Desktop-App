package app.controller;

import app.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class helloPageController {

    @FXML
    private Label nameinputField;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null && nameinputField != null) {
            nameinputField.setText(user.getFullName());
        } else {
            System.out.println("Error when getting current user");
        }
    }

    @FXML
    private void startaction(ActionEvent event) {
        if (currentUser == null) {
            showAlert("No active user. Please log in again.");
            return;
        }
        navigateTo(event, "/app/view/mytaskPage.fxml", "Failed to open tasks page");
    }

    @FXML
    private void aboutAction(ActionEvent event) {
        navigateTo(event, "/app/view/aboutPage.fxml", "Failed to open About page");
    }

    @FXML
    private void changeAction(ActionEvent event) {
        navigateTo(event, "/app/view/changepasswordPage.fxml", "Failed to open Change Password page");
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof RequiresUser) {
                ((RequiresUser) controller).setCurrentUser(currentUser);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(errorMessage + ": " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Navigation");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public interface RequiresUser {
        void setCurrentUser(User user);
    }
}
