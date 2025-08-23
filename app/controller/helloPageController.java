package app.controller;

import app.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class helloPageController {

    @FXML
    private Label nameinputField;

    private User currentUser;

    // يُستدعى من شاشة تسجيل الدخول
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            nameinputField.setText(user.getFullName());
        } else {
            System.out.println("⚠️ helloPageController.setCurrentUser: user == null");
        }
    }

    // زر Start: ينقلك مباشرة إلى صفحة المهام
    @FXML
    private void startaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/mytaskPage.fxml"));
            Parent root = loader.load();

            mytaskPageController controller = loader.getController();
            controller.setCurrentUser(currentUser); // مرري المستخدم

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // آمن لأنه جاي من زر
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
