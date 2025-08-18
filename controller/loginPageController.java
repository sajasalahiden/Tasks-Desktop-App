package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginPageController implements Initializable {


    @FXML private TextField emailField;
    @FXML private TextField passFeild;

    /**
     * يتم استدعاؤها تلقائيًا عند الضغط على زر تسجيل الدخول من الواجهة
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passFeild.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in both fields.");
            return;
        }

        User user = UserStorage.getUserByEmailAndPassword(email, password);
        if (user != null) {
            goToHelloPage(event, user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Incorrect email or password.");
        }
    }

    /**
     * يتم استدعاؤها تلقائيًا عند الضغط على Hyperlink "Create one"
     */
    @FXML
    private void goToSignupPage(javafx.event.ActionEvent event) {
        try {
            Parent signupRoot = FXMLLoader.load(getClass().getResource("/view/signupPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToHelloPage(javafx.event.ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/helloPage.fxml"));
            Parent root = loader.load();

//             في حال أردت إرسال معلومات المستخدم للصفحة التالية
             helloPageController controller = loader.getController();
             controller.setCurrentUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
