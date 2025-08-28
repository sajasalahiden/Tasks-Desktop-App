// app/model/User.java
package app.model;

import java.util.Objects;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String passwordHash;

    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (email == null || email.isBlank()) ? null : email.trim();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;

        if (id != null && u.id != null) return Objects.equals(id, u.id);

        if (email != null && u.email != null) return email.equalsIgnoreCase(u.email);

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
    }
}
