// app/model/User.java
package app.model;

import app.security.Hashing;
import java.util.Objects;

public class User {
    private Long id;
    private String fullName;
    private String email;          // UNIQUE في DB
    private String passwordHash;   // يخزن SHA-256 أو أي هاش آخر

    public User() {}

    public User(Long id, String fullName, String email, String passwordHash) {
        this.id = id;
        setFullName(fullName);
        setEmail(email);
        this.passwordHash = passwordHash;
    }

    /** مُنشئ مريح من كلمة سر خام (يحسب الهاش مباشرة). */
    public static User withRawPassword(Long id, String fullName, String email, String rawPassword) {
        User u = new User();
        u.setId(id);
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPasswordRaw(rawPassword);
        return u;
    }

    // ===== Getters / Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = (email == null || email.isBlank()) ? null : email.trim();
    }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /** قبول كلمة سر خام وتحويلها لهاش. */
    public void setPasswordRaw(String rawPassword) {
        if (rawPassword == null) throw new IllegalArgumentException("raw password cannot be null");
        this.passwordHash = Hashing.sha256(rawPassword);
    }

    // توافق للخلف مع أي كود قديم كان يستخدم getPassword()
    /** @deprecated استخدم getPasswordHash بدلاً منه. */
    @Deprecated
    public String getPassword() { return getPasswordHash(); }
    /** @deprecated استخدم setPasswordHash أو setPasswordRaw. */
    @Deprecated
    public void setPassword(String passwordHash) { setPasswordHash(passwordHash); }

    // ===== Identity & Debug =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;

        // أولاً: المعرّف من DB
        if (id != null && u.id != null) return Objects.equals(id, u.id);

        // ثانياً: البريد
        if (email != null && u.email != null) return email.equalsIgnoreCase(u.email);

        // كحل أخير: الاسم الكامل
        return Objects.equals(fullName, u.fullName);
    }

    @Override
    public int hashCode() {
        if (id != null) return Objects.hash(id);
        if (email != null) return email.toLowerCase().hashCode();
        return Objects.hash(fullName);
    }

    @Override
    public String toString() {
        return "User{id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
        // لا نطبع passwordHash طبعًا
    }
}
