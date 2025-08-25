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

    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private Label dueLabel;

    @FXML private Circle statusCircle;  // الدائرة يسار العنوان
    @FXML private Label checkIcon;      // علامة ✓ داخل الدائرة

    private Task task;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setTask(Task task) {
        this.task = task;
        if (task == null) return;

        // نصوص البطاقة
        titleLabel.setText(s(task.getTitle()));
        descLabel.setText(s(task.getDescription()));

        if (task.getDueDate() != null) {
            dueLabel.setText(task.getDueDate().format(DTF));
        } else {
            dueLabel.setText("");
        }

        // مظهر الاكتمال + التلوين حسب الأولوية + وسم التأخير
        renderUI();
    }

    /** Call this بعد تعديل task من شاشة أخرى لتحديث الكارد بدون إعادة تحميل الـ FXML. */
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

    /** تلوين حدود الدائرة حسب الأولوية (لا نغيّر النصوص ولا الـ fx:id). */
    private void renderPriority(Priority p) {
        if (statusCircle == null) return;
        String strokeColor;
        if (p == null) p = Priority.MEDIUM;

        switch (p) {
            case HIGH -> strokeColor = "#e11d48";   // أحمر
            case MEDIUM -> strokeColor = "#f59e0b"; // برتقالي
            case LOW -> strokeColor = "#9ca3af";    // رمادي
            default -> strokeColor = "#9ca3af";
        }

        // نحافظ على الـ fill من renderCompleted ونغيّر الـ stroke فقط
        String current = statusCircle.getStyle();
        String base = (current == null ? "" : current.replaceAll("-fx-stroke:\\s*#[0-9a-fA-F]{6};?", ""));
        statusCircle.setStyle(base + " -fx-stroke: " + strokeColor + ";");
    }

    /** يوسم التاريخ لو المهمة متأخرة وغير مكتملة. */
    private void renderOverdueBadge(LocalDate due, boolean completed) {
        if (dueLabel == null) return;

        if (!completed && due != null && due.isBefore(LocalDate.now())) {
            // متأخرة
            dueLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
            if (!dueLabel.getText().contains(" (Overdue)")) {
                dueLabel.setText(dueLabel.getText() + " (Overdue)");
            }
        } else {
            // عادي
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
        if (p == null) p = Priority.MEDIUM;
        String msg = (completed ? "Completed" : "Not completed") + " • Priority: " + p.name();
        Tooltip.install(statusCircle, new Tooltip(msg));
    }

    private static String s(String x) { return x == null ? "" : x; }
}
