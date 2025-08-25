package app.controller;

import app.model.Task;
import app.model.User;

import app.config.ServiceLocator;
import app.repo.TaskRepository;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class mytaskPageController {

    @FXML private VBox tasksContainer;
    @FXML private Label noTasksFeild;
    @FXML private Button addBtn;

    private User currentUser;

    // مستودع المهام من ServiceLocator (بديل TaskStorage)
    private final TaskRepository tasksRepo = ServiceLocator.tasks();

    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (user != null) {
            System.out.println("✅ currentUser وُصل إلى mytaskPageController: " + user);
            initializeTasks();
        } else {
            System.out.println("⚠️ mytaskPageController.setCurrentUser: user == null");
            tasksContainer.getChildren().clear();
            showNoTasksMessage("Please log in to see your tasks");
        }
    }

    @FXML
    private void newtaskBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/newtaskPage.fxml"));
            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTasks() {
        if (currentUser == null || currentUser.getId() == null) {
            System.out.println("⚠️ initializeTasks: currentUser == null أو لا يملك id");
            showNoTasksMessage("Please log in to see your tasks");
            return;
        }

        try {
            List<Task> userTasks = tasksRepo.listByUser(currentUser.getId());

            if (userTasks == null || userTasks.isEmpty()) {
                showNoTasksMessage("No Tasks Yet");
                return;
            }

            showTasks(userTasks);
        } catch (Exception ex) {
            ex.printStackTrace();
            showNoTasksMessage("Failed to load tasks");
        }
    }

    private void showNoTasksMessage(String text) {
        if (text != null) noTasksFeild.setText(text);
        noTasksFeild.setVisible(true);
        noTasksFeild.setManaged(true);
        tasksContainer.getChildren().setAll(noTasksFeild);
    }

    private void showTasks(List<Task> tasks) {
        noTasksFeild.setVisible(false);
        noTasksFeild.setManaged(false);
        tasksContainer.getChildren().clear();

        for (Task task : tasks) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/cardPage.fxml"));
                Node taskCard = loader.load();

                TaskCardController cardController = loader.getController();
                if (cardController != null) {
                    cardController.setTask(task);
                }

                // افتح التفاصيل عند النقر
                taskCard.setOnMouseClicked(e -> openTaskDetails(task));
                taskCard.setStyle(taskCard.getStyle() + "; -fx-cursor: hand;");

                tasksContainer.getChildren().add(taskCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // يفتح صفحة تفاصيل المهمة ويمرّر user + task
    private void openTaskDetails(Task task) {
        try {
            FXMLLoader fx = new FXMLLoader(getClass().getResource("/app/view/viewCardPage.fxml"));
            Parent root = fx.load();

            TaskDetailsController c = fx.getController();
            if (c != null) {
                c.setContext(currentUser, task);
            }

            Stage st = (Stage) tasksContainer.getScene().getWindow();
            st.setScene(new Scene(root));
            st.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
