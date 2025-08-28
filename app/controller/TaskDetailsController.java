package app.controller;

import app.model.Priority;
import app.model.Task;
import app.model.User;

import app.config.ServiceLocator;
import app.repo.TaskRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;

public class TaskDetailsController {

    @FXML
    private Label titleBtn;
    @FXML
    private Label desLbl;
    @FXML
    private Label dateLbl;
    @FXML
    private Label PriorityLbl;
    @FXML
    private Circle colorCircle;
    @FXML
    private CheckBox completeMark;
    @FXML
    private Button backBtn, deleteBtn, editBtn;

    private TextField titleField;
    private TextArea descArea;
    private DatePicker datePicker;
    private ComboBox<Priority> priorityBox;

    private Label titleCaption;

    private User currentUser;
    private Task currentTask;
    private boolean editing = false;

    private final TaskRepository tasksRepo = ServiceLocator.tasks();

    public void setContext(User user, Task task) {
        this.currentUser = user;
        this.currentTask = task;
        if (task != null) fillLabelsFromTask(task);

        if (currentTask != null) {
            completeMark.setSelected(currentTask.isCompleted());
            completeMark.selectedProperty().addListener((obs, o, v) -> {
                currentTask.setCompleted(v);
                persistUpdateSilently();
            });
        }
    }

    @FXML
    private void onBack() {
        goToMyTasks();
    }

    @FXML
    private void onDelete() {
        if (currentUser == null || currentUser.getId() == null || currentTask == null || currentTask.getId() == null) {
            alert("Delete Task", "Missing user or task id.");
            return;
        }
        try {
            tasksRepo.delete(currentTask.getId(), currentUser.getId());
            goToMyTasks();
        } catch (Exception ex) {
            ex.printStackTrace();
            alert("Delete Task", "Failed to delete: " + ex.getMessage());
        }
    }

    @FXML
    private void onEditOrSave(ActionEvent e) {
        if (!editing) {
            enterEditMode();
        } else if (applyEditsToTask()) {
            if (!persistUpdate()) return;
            exitEditMode();
            fillLabelsFromTask(currentTask);
        }
    }

    private void fillLabelsFromTask(Task t) {
        titleBtn.setText(safe(t.getTitle(), "Task Details"));
        desLbl.setText(safe(t.getDescription(), ""));
        dateLbl.setText(t.getDueDate() != null ? t.getDueDate().toString() : "-");

        Priority p = t.getPriority();
        PriorityLbl.setText(p != null ? display(p) : "-");
        updatePriorityColor(p);
    }

    private void enterEditMode() {
        if (currentTask == null) return;
        editing = true;
        editBtn.setText("Save");

        if (titleCaption == null) {
            titleCaption = new Label("Title");
            titleCaption.setStyle("-fx-font-weight: bold;");
        }
        if (titleBtn.getParent() instanceof VBox vb) {
            int idx = vb.getChildren().indexOf(titleBtn);
            if (idx >= 0 && !vb.getChildren().contains(titleCaption)) {
                vb.getChildren().add(idx, titleCaption);
                VBox.setMargin(titleCaption, new Insets(0, 0, 0, 20));
            }
        }

        if (titleField == null) {
            titleField = new TextField();
            titleField.setPromptText("Title");
            titleField.setMaxWidth(Double.MAX_VALUE);
        }
        titleField.setText(currentTask.getTitle());
        replaceNode(titleBtn, titleField);
        if (titleField.getParent() instanceof VBox)
            VBox.setMargin(titleField, new Insets(0, 10, 0, 20));

        if (descArea == null) {
            descArea = new TextArea();
            descArea.setPrefRowCount(3);
            descArea.setWrapText(true);
            descArea.setMaxWidth(Double.MAX_VALUE);
        }
        descArea.setText(currentTask.getDescription());
        replaceNode(desLbl, descArea);
        if (descArea.getParent() instanceof VBox)
            VBox.setMargin(descArea, new Insets(0, 10, 0, 20));

        if (datePicker == null) {
            datePicker = new DatePicker();
            datePicker.setPrefWidth(200);
            datePicker.setMaxWidth(200);
        }
        datePicker.setValue(currentTask.getDueDate());
        replaceNode(dateLbl, datePicker);
        if (datePicker.getParent() instanceof HBox hb) {
            HBox.setMargin(datePicker, new Insets(10, 0, 0, 10));
            ensureHeadingLabelVisible(hb, "Due Date");
        }

        // ---- Priority ----
        if (priorityBox == null) {
            priorityBox = new ComboBox<>();
            priorityBox.getItems().setAll(Priority.values());
            priorityBox.setPrefWidth(160);
            priorityBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Priority p) {
                    if (p == null) return "";
                    return switch (p) {
                        case HIGH -> "High";
                        case MEDIUM -> "Medium";
                        case LOW -> "Low";
                    };
                }

                @Override
                public Priority fromString(String s) {
                    if (s == null) return null;
                    return switch (s.trim().toLowerCase()) {
                        case "high" -> Priority.HIGH;
                        case "medium" -> Priority.MEDIUM;
                        case "low" -> Priority.LOW;
                        default -> null;
                    };
                }
            });
        }
        priorityBox.getSelectionModel().select(currentTask.getPriority());
        replaceNode(PriorityLbl, priorityBox);
        if (priorityBox.getParent() instanceof HBox hb) {
            HBox.setMargin(priorityBox, new Insets(10, 0, 0, 10));
            ensureHeadingLabelVisible(hb, "Priority");
        }
    }

    private void exitEditMode() {
        editing = false;
        editBtn.setText("Edit Task");

        if (titleCaption != null && titleCaption.getParent() instanceof VBox vb) {
            vb.getChildren().remove(titleCaption);
        }

        titleBtn.setText(safe(currentTask.getTitle(), "Task Details"));
        replaceNode(titleField, titleBtn);

        desLbl.setText(safe(currentTask.getDescription(), ""));
        replaceNode(descArea, desLbl);

        dateLbl.setText(currentTask.getDueDate() != null ? currentTask.getDueDate().toString() : "-");
        replaceNode(datePicker, dateLbl);

        Priority p = currentTask.getPriority();
        PriorityLbl.setText(p != null ? display(p) : "-");
        replaceNode(priorityBox, PriorityLbl);
    }

    private boolean applyEditsToTask() {
        if (currentTask == null) return false;

        String title = (titleField != null && titleField.getText() != null) ? titleField.getText().trim() : "";
        if (title.isEmpty()) {
            alert("Validation", "Title is required.");
            return false;
        }

        String desc = (descArea != null && descArea.getText() != null) ? descArea.getText().trim() : "";
        LocalDate date = datePicker != null ? datePicker.getValue() : currentTask.getDueDate();
        Priority priority = priorityBox != null ? priorityBox.getSelectionModel().getSelectedItem() : currentTask.getPriority();
        if (priority == null) priority = currentTask.getPriority();

        currentTask.setTitle(title);
        currentTask.setDescription(desc);
        currentTask.setDueDate(date);
        currentTask.setPriority(priority);

        try {
            if (currentTask.getUserId() == null && currentUser != null && currentUser.getId() != null) {
                currentTask.setUserId(currentUser.getId());
            }
        } catch (Throwable ignored) {
        }

        updatePriorityColor(priority);
        return true;
    }

    private boolean persistUpdate() {
        if (currentTask.getId() == null || currentTask.getUserId() == null) {
            alert("Save Task", "Missing task id or user id.");
            return false;
        }
        try {
            tasksRepo.update(currentTask);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            alert("Save Task", "Failed to save: " + ex.getMessage());
            return false;
        }
    }

    private void persistUpdateSilently() {
        try {
            if (currentTask != null && currentTask.getId() != null) {
                if (currentTask.getUserId() == null && currentUser != null) {
                    currentTask.setUserId(currentUser.getId());
                }
                if (currentTask.getUserId() != null) tasksRepo.update(currentTask);
            }
        } catch (Exception ignored) {
        }
    }

    private void updatePriorityColor(Priority p) {
        colorCircle.getStyleClass().removeAll("prio-high", "prio-medium", "prio-low");
        if (p == null) {
            colorCircle.setStyle("-fx-fill: #D1D5DB;");
            return;
        }
        switch (p) {
            case HIGH -> {
                colorCircle.setStyle("-fx-fill: #EF4444;");
                colorCircle.getStyleClass().add("prio-high");
            }
            case MEDIUM -> {
                colorCircle.setStyle("-fx-fill: #F59E0B;");
                colorCircle.getStyleClass().add("prio-medium");
            }
            case LOW -> {
                colorCircle.setStyle("-fx-fill: #22C55E;");
                colorCircle.getStyleClass().add("prio-low");
            }
        }
    }

    private void goToMyTasks() {
        try {
            FXMLLoader fx = new FXMLLoader(getClass().getResource("/app/view/mytaskPage.fxml"));
            Parent root = fx.load();
            mytaskPageController c = fx.getController();
            if (currentUser != null) c.setCurrentUser(currentUser);
            Stage st = (Stage) backBtn.getScene().getWindow();
            st.setScene(new Scene(root));
            st.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ====== أدوات ======
    private void replaceNode(javafx.scene.Node oldNode, javafx.scene.Node newNode) {
        if (oldNode == null || newNode == null || oldNode.getParent() == null) return;
        if (oldNode.getParent() instanceof VBox parentV) {
            int idx = parentV.getChildren().indexOf(oldNode);
            if (idx >= 0) parentV.getChildren().set(idx, newNode);
        } else if (oldNode.getParent() instanceof HBox parentH) {
            int idx = parentH.getChildren().indexOf(oldNode);
            if (idx >= 0) parentH.getChildren().set(idx, newNode);
        }
        newNode.setFocusTraversable(true);
    }

    private void ensureHeadingLabelVisible(HBox hb, String expectedText) {
        for (javafx.scene.Node n : hb.getChildren()) {
            if (n instanceof Label l) {
                String txt = l.getText() == null ? "" : l.getText().trim();
                if (txt.toLowerCase().startsWith(expectedText.toLowerCase().substring(0, 3))) {
                    l.setWrapText(false);
                    l.setMinWidth(Region.USE_PREF_SIZE);
                    l.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    l.setMaxWidth(Region.USE_COMPUTED_SIZE);
                    l.setText(expectedText);
                }
            }
        }
    }

    private String safe(String s, String def) {
        return (s == null || s.isBlank()) ? def : s;
    }

    private String display(Priority p) {
        return switch (p) {
            case HIGH -> "High";
            case MEDIUM -> "Medium";
            case LOW -> "Low";
        };
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}
