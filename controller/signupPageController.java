package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class signupPageController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField emailField2;
    @FXML private TextField passFeild;

    // هذا يستدعى تلقائياً عند تحميل الواجهة إذا أردت إعدادات إضافية

    @FXML
    private void handleSignup(javafx.event.ActionEvent event) {
        String fullName = nameField.getText().trim();
        String email = emailField2.getText().trim();
        String password = passFeild.getText().trim();

        StringBuilder errors = new StringBuilder();

        // التحقق من الحقول الفارغة
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errors.append("- Please fill in all fields.\n");
        }

        // التحقق من الاسم: يحتوي على حروف
        if (!fullName.matches(".*[a-zA-Zأ-ي].*")) {
            errors.append("- Name must contain letters.\n");
        }

        // التحقق من صيغة الإيميل
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.append("- Invalid email format.\n");
        }

        // التحقق من طول الباسورد
        if (password.length() < 7) {
            errors.append("- Password must be at least 7 characters long.\n");
        }

        // التحقق من أن الإيميل غير مسجل مسبقاً
        if (UserStorage.getUserByEmail(email) != null) {
            errors.append("- Email already registered.\n");
        }

        // إذا كان هناك أخطاء، نعرضها كلها مرة واحدة
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return;
        }

        // إذا لم تكن هناك أخطاء: تسجيل المستخدم
        User newUser = new User(null, fullName, password, email);
        UserStorage.addUser(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Account created successfully!");
        goToLoginPage(event);
    }



    @FXML
    private void handleLogin(javafx.event.ActionEvent event) {
        goToLoginPage(event);
    }

    private void goToLoginPage(javafx.event.ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load login page.");
        }
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
