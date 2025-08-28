package app.controller;

import app.model.Task;
import app.model.Priority;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskCardController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label dueLabel;

    @FXML
    private Circle statusCircle;
    @FXML
    private Label checkIcon;

    private Task task;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setTask(Task task) {
        this.task = task;
        if (task == null) return;

        titleLabel.setText(s(task.getTitle()));
        descLabel.setText(s(task.getDescription()));

        if (task.getDueDate() != null) {
            dueLabel.setText(task.getDueDate().format(DTF));
        } else {
            dueLabel.setText("");
        }

        renderUI();
    }

    /**
     * Call this بعد تعديل task من شاشة أخرى لتحديث الكارد بدون إعادة تحميل الـ FXML.
     */
    public void refresh() {
        if (this.task != null) setTask(this.task);
    }

    // ===== داخلي =====

    private void renderUI() {
        boolean completed = task.isCompleted();
        renderCompleted(completed);
        renderPriority(task.getPriority());
        renderOverdueBadge(task.getDueDate(), completed);
        attachTooltip(completed, task.getPriority());
    }

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

    private void renderPriority(Priority p) {
        if (statusCircle == null) return;
        String strokeColor;

        switch (p) {
            case HIGH -> strokeColor = "#e11d48";
            case MEDIUM -> strokeColor = "#f59e0b";
            case LOW -> strokeColor = "#9ca3af";
            default -> strokeColor = "#9ca3af";
        }

        String current = statusCircle.getStyle();
        String base = (current == null ? "" : current.replaceAll("-fx-stroke:\\s*#[0-9a-fA-F]{6};?", ""));
        statusCircle.setStyle(base + " -fx-stroke: " + strokeColor + ";");
    }

    private void renderOverdueBadge(LocalDate due, boolean completed) {
        if (dueLabel == null) return;

        if (!completed && due != null && due.isBefore(LocalDate.now())) {
            dueLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
            if (!dueLabel.getText().contains(" (Overdue)")) {
                dueLabel.setText(dueLabel.getText() + " (Overdue)");
            }
        } else {

            dueLabel.setStyle(null);
            if (due != null) {
                dueLabel.setText(due.format(DTF));
            } else {
                dueLabel.setText("");
            }
        }
    }

    private void attachTooltip(boolean completed, Priority p) {
        if (statusCircle == null) return;
        String msg = (completed ? "Completed" : "Not completed") + " • Priority: " + p.name();
        Tooltip.install(statusCircle, new Tooltip(msg));
    }

    private static String s(String x) {
        return x == null ? "" : x;
    }
}
