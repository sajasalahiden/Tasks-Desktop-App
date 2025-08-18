package model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private String id = UUID.randomUUID().toString();

    private String title;
    private String description;
    private LocalDate dueDate;       // يمكنك تغييره إلى LocalDate إذا كنت تستخدم DatePicker
    private Priority priority;
    private User user;

    private boolean isCompleted;  // لإظهار علامة الصح لاحقًا

    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = false; // الحالة الافتراضية: غير مكتمل
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", isCompleted=" + isCompleted +
                ", user=" + (user != null ? user.getFullName() : "null") +
                '}';
    }

}
