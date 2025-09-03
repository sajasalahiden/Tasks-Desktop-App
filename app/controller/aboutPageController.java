package app.controller;

import app.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class aboutPageController implements helloPageController.RequiresUser {

    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void backAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/helloPage.fxml"));
            Parent root = loader.load();

            helloPageController helloCtrl = loader.getController();
            if (currentUser != null) {
                helloCtrl.setCurrentUser(currentUser);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading helloPage: " + e.getMessage());
        }
    }
}
