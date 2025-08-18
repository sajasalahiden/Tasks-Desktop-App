package controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;

public class helloPageController {
    @FXML
    private Label nameinputField;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        nameinputField.setText(user.getFullName());

        // Start animation or timed redirection
        startRedirectTimer();
    }

    private void startRedirectTimer() {
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> goToAllTasksPage());
        delay.play();
    }

    private void goToAllTasksPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/allTasksPage.fxml"));
            Parent root = loader.load();

            // Optional: إرسال المستخدم للصفحة الجديدة إذا احتجت ذلك
            // AllTasksPageController controller = loader.getController();
            // controller.setCurrentUser(currentUser);

            Stage stage = (Stage) nameinputField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
