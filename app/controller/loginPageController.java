package app.controller;

import app.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginPageController implements Initializable {

    @FXML private TextField emailField;
    @FXML private TextField passFeild; // تأكد أن fx:id في FXML هو نفسه "passFeild"

    // عند النقر على زر "Login"
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passFeild.getText() == null ? "" : passFeild.getText().trim();

        // تحقق من عدم ترك الحقول فارغة
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in both fields.");
            return;
        }

        // تحقق من صحة البيانات عبر UserStorage
        User user = app.controller.UserStorage.getUserByEmailAndPassword(email, password);

        if (user != null) {
            // ✅ إذا كانت البيانات صحيحة، انتقل إلى helloPage ومرّر المستخدم
            goToHelloPage(event, user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Incorrect email or password.");
        }
    }

    // الانتقال إلى صفحة إنشاء حساب
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

    // الانتقال إلى صفحة الترحيب HelloPage مع تمرير المستخدم لها
    private void goToHelloPage(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/helloPage.fxml"));
            Parent root = loader.load();

            // ✅ إرسال المستخدم لصفحة helloPage
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

    // عرض رسالة تنبيه (خطأ أو نجاح)
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // لا حاجة لشيء الآن
    }
}
