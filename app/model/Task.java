package app.model;

import java.time.LocalDate;

public class Task {

    private Long id;            // BIGINT في DB
    private Long userId;        // BIGINT في DB

    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    public Priority priority;

    // للاستخدام المؤقت في الواجهة فقط
    private User user;          // اختياري: اربطيه في UI، لكن الحفظ يتم عبر userId

    public Task() {}

    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        if (priority != null) this.priority = priority;
        this.completed = false;
    }

    // ===== Getters / Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = (priority != null ? priority : Priority.MEDIUM); }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public User getUser() { return user; }
    public void setUser(User user) {
        this.user = user;
        // مزامنة اختيارية: لو عندك user وفيه id، خليه يملأ userId
        try {
            if (user != null && user.getId() != null) this.userId = user.getId();
        } catch (Throwable ignored) {}
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", completed=" + completed +
                ", user=" + (user != null ? user.getFullName() : "null") +
                '}';
    }
}
