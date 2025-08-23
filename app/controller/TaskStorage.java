package app.model;

import java.util.*;
import java.util.stream.Collectors;

public class TaskStorage {

    // نخزن كل المهام في قائمة واحدة
    private static final List<Task> tasks = new ArrayList<>();

    /** إضافة مهمة (تُهمل null). */
    public static synchronized void addTask(Task task) {
        if (task != null) tasks.add(task);
    }

    /** ترجع نسخة للقراءة فقط من كل المهام. */
    public static synchronized List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    /** ترجع مهام مستخدم محدد. */
    public static synchronized List<Task> getTasksForUser(app.model.User user) {
        if (user == null) return Collections.emptyList();
        return tasks.stream()
                .filter(Objects::nonNull)
                .filter(t -> sameUser(t.getUser(), user))
                .collect(Collectors.toList());
    }

    /** هل القائمة فارغة؟ */
    public static synchronized boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** حذف كل المهام (للاختبارات). */
    public static synchronized void clearTasks() {
        tasks.clear();
    }

    /** حذف مهمة لمستخدم محدد. يرجّع true إذا تم الحذف. */
    public static synchronized boolean deleteTask(app.model.User user, Task task) {
        if (task == null) return false;

        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            Task t = it.next();
            if (t == null) continue;
            if (!sameUser(t.getUser(), user)) continue;

            // حذف بالهوية/equals أو بمطابقة الحقول الأساسية
            if (t == task || Objects.equals(t, task)
                    || (Objects.equals(t.getTitle(), task.getTitle())
                    && Objects.equals(t.getDescription(), task.getDescription())
                    && Objects.equals(t.getDueDate(), task.getDueDate())
                    && Objects.equals(norm(t.getPriority().toString()), norm(task.getPriority().toString())))) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /** حذف مهمة بالمعرّف (إن كان Task يوفّر getId()). */
    public static synchronized boolean deleteTaskById(app.model.User user, String taskId) {
        if (taskId == null) return false;
        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            Task t = it.next();
            if (t == null) continue;
            if (!sameUser(t.getUser(), user)) continue;
            try {
                if (Objects.equals(t.getId(), taskId)) {
                    it.remove();
                    return true;
                }
            } catch (Throwable ignored) { /* لو ما عندك getId() تجاهلي */ }
        }
        return false;
    }

    // ====== أدوات داخلية ======

    private static boolean sameUser(app.model.User a, app.model.User b) {
        if (a == b) return true;
        if (a == null || b == null) return false;

        // لو عندك id للمستخدم، يفضل المقارنة به
        try {
            Object ida = a.getId();
            Object idb = b.getId();
            if (ida != null && idb != null) return Objects.equals(ida, idb);
        } catch (Throwable ignored) { }

        // وإلا نقارن بالإيميل
        try {
            String ea = a.getEmail();
            String eb = b.getEmail();
            if (ea != null && eb != null) return ea.equalsIgnoreCase(eb);
        } catch (Throwable ignored) { }

        // وإلا fallback إلى equals
        return a.equals(b);
    }

    private static String norm(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}
