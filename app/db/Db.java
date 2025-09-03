// app/db/Db.java
package app.db;

import java.sql.*;

public final class Db {

    private static final String URL =
            "jdbc:mysql://localhost:3306/tasksdb"
                    + "?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("MySQL Driver not found", e); }
        init();
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    private static void init() {
        try (Connection c = get(); Statement st = c.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users(
                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                  full_name VARCHAR(255) NOT NULL,
                  email VARCHAR(255) NOT NULL UNIQUE,
                  password_hash VARCHAR(255) NOT NULL
                )
            """);
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS tasks(
                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                  user_id BIGINT NOT NULL,
                  title VARCHAR(255) NOT NULL,
                  description TEXT,
                  due_date DATE,
                  priority ENUM('HIGH','MEDIUM','LOW'),
                  completed TINYINT(1) NOT NULL DEFAULT 0,
                  CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }


}
