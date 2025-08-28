// app/repo/mysql/MySqlTaskRepository.java
package app.repo.mysql;

import app.db.Db;
import app.model.Task;
import app.repo.TaskRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlTaskRepository implements TaskRepository {

    private static app.model.Priority coercePriority(String p) {
        if (p == null) return app.model.Priority.MEDIUM;
        try { return app.model.Priority.valueOf(p.trim().toUpperCase()); }
        catch (IllegalArgumentException ex) { return app.model.Priority.MEDIUM; }
    }

    private Task map(ResultSet rs) throws SQLException {
        var t = new Task();
        t.setId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));

        java.sql.Date d = rs.getDate("due_date");
        t.setDueDate(d != null ? d.toLocalDate() : null);

        t.setPriority(coercePriority(rs.getString("priority")));
        t.setCompleted(rs.getBoolean("completed"));
        return t;
    }

    @Override
    public List<Task> listByUser(long userId) throws Exception {
        final String sql =
                "SELECT id,user_id,title,description,due_date,priority,completed " +
                        "FROM tasks WHERE user_id=? " +
                        "ORDER BY completed ASC, due_date IS NULL ASC, due_date ASC";

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Task> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    @Override
    public long save(Task t) throws Exception {
        if (t.getUserId() == null && t.getUser() != null && t.getUser().getId() != null) {
            t.setUserId(t.getUser().getId());
        }
        if (t.getUserId() == null) throw new IllegalArgumentException("userId is required");
        if (t.getPriority() == null) t.setPriority(app.model.Priority.MEDIUM);

        final String sql =
                "INSERT INTO tasks(user_id,title,description,due_date,priority,completed) " +
                        "VALUES (?,?,?,?,?,?)";

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, t.getUserId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getDescription());
            if (t.getDueDate() != null) {
                ps.setDate(4, java.sql.Date.valueOf(t.getDueDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, t.getPriority().name());
            ps.setBoolean(6, t.isCompleted());

            ps.executeUpdate();

            try (ResultSet k = ps.getGeneratedKeys()) {
                if (k.next()) {
                    long id = k.getLong(1);
                    t.setId(id);
                    return id;
                }
            }
            throw new SQLException("No generated key");
        }
    }

    @Override
    public void update(Task t) throws Exception {
        if (t.getId() == null) throw new IllegalArgumentException("id is required");
        if (t.getUserId() == null) throw new IllegalArgumentException("userId is required");

        final String sql =
                "UPDATE tasks SET title=?, description=?, due_date=?, priority=?, completed=? " +
                        "WHERE id=? AND user_id=?";

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            if (t.getDueDate() != null) {
                ps.setDate(3, java.sql.Date.valueOf(t.getDueDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setString(4, (t.getPriority() != null ? t.getPriority() : app.model.Priority.MEDIUM).name());
            ps.setBoolean(5, t.isCompleted());
            ps.setLong(6, t.getId());
            ps.setLong(7, t.getUserId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id, long userId) throws Exception {
        final String sql = "DELETE FROM tasks WHERE id=? AND user_id=?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }
}
