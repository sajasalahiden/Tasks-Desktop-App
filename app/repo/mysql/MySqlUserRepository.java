package app.repo.mysql;

import app.db.Db;
import app.model.User;
import app.repo.UserRepository;

import java.sql.*;
import java.util.*;

public class MySqlUserRepository implements UserRepository {
    private User map(ResultSet rs) throws SQLException {
        var u = new User();
        u.setId(rs.getLong("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        return u;
    }

    @Override
    public Optional<User> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email=?";
        try (var c = Db.get(); var ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public Optional<User> findById(long id) throws Exception {
        String sql = "SELECT * FROM users WHERE id=?";
        try (var c = Db.get(); var ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<User> findAll() throws Exception {
        try (var c = Db.get(); var st = c.createStatement(); var rs = st.executeQuery("SELECT * FROM users")) {
            var out = new ArrayList<User>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public long save(User u) throws Exception {
        String sql = "INSERT INTO users(full_name,email,password_hash) VALUES (?,?,?)";
        try (var c = Db.get(); var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.executeUpdate();
            try (var k = ps.getGeneratedKeys()) {
                if (k.next()) return k.getLong(1);
                throw new SQLException("No generated key");
            }
        }
    }

    @Override
    public void update(User u) throws Exception {
        String sql = "UPDATE users SET full_name=?, email=?, password_hash=? WHERE id=?";
        try (var c = Db.get(); var ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setLong(4, u.getId());
            ps.executeUpdate();
        }
    }
}
