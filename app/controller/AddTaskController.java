package app.controller;

import app.model.Priority;
import app.model.Task;
import app.model.User;

import app.config.ServiceLocator;
import app.repo.TaskRepository;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    @FXML
    private TextField taskField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<Priority> priorityField;

    @FXML
    private Button savaBtn;

    private User currentUser;

    private final TaskRepository tasksRepo = ServiceLocator.tasks();

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    void saveAction(ActionEvent event) {
        String title = taskField.getText();
        String description = descriptionField.getText();
        LocalDate dueDate = dateField.getValue();
        Priority priority = priorityField.getValue();

        if (title == null || title.isEmpty() || dueDate == null || priority == null) {
            showAlert("Please fill in all required fields.");
            return;
        }
        if (currentUser == null || currentUser.getId() == null) {
            showAlert("No active user. Please log in again.");
            return;
        }

        try {
            Task task = new Task(title, description, dueDate, priority);
            task.setUser(currentUser);
            try {
                if (task.getUserId() == null && currentUser.getId() != null) {
                    task.setUserId(currentUser.getId());
                }
            } catch (Throwable ignored) {
            }

            tasksRepo.save(task);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/mytaskPage.fxml"));
            Parent root = loader.load();

            mytaskPageController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) savaBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to save task: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityField.setItems(FXCollections.observableArrayList(Priority.values()));
    }
}
