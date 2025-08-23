package app.controller;

import app.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class TaskCardController {

    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private Label dueLabel;

    @FXML private Circle statusCircle;  // الدائرة يسار العنوان
    @FXML private Label checkIcon;      // علامة ✓ داخل الدائرة

    private Task task;

    public void setTask(Task task) {
        this.task = task;
        if (task == null) return;

        // نصوص البطاقة
        titleLabel.setText(task.getTitle() != null ? task.getTitle() : "");
        descLabel.setText(task.getDescription() != null ? task.getDescription() : "");
        dueLabel.setText(task.getDueDate() != null ? task.getDueDate().toString() : "");

        // مظهر الاكتمال
        renderCompleted(task.isCompleted());
    }

    /** يضبط مظهر الدائرة / الصح حسب حالة الاكتمال. */
    private void renderCompleted(boolean completed) {
        if (statusCircle == null || checkIcon == null) return;

        if (completed) {
            statusCircle.setStyle("-fx-fill: #67CE67; -fx-stroke: #D1D5DB;");
            checkIcon.setVisible(true);
            checkIcon.setManaged(true);
        } else {
            statusCircle.setStyle("-fx-fill: white; -fx-stroke: #D1D5DB;");
            checkIcon.setVisible(false);
            checkIcon.setManaged(false);
        }
    }
}
