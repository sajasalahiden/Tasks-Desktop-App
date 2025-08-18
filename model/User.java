package model;


public class User {
        private Long id;
        private String fullName;
        private String password; // TODO: استبدال لاحقًا بـ passwordHash
        private String email;

        public User() { }

        public User(Long id, String username, String password) {
            this(id, username, password, null);
        }

        public User(Long id, String username, String password, String email) {
            this.id = id;
            this.fullName = username;
            this.password = password;
            this.email = email;
        }

        // Getters / Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName != null ? fullName.trim() : null; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = (email != null && !email.isBlank()) ? email.trim() : null; }

        // مساواة وهوية مبنية على اسم المستخدم (مناسب للتجارب والتخزين المؤقت)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User u)) return false;
            return fullName != null && fullName.equals(u.fullName);
        }

        @Override
        public int hashCode() {
            return fullName == null ? 0 : fullName.hashCode();
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", fullName='" + fullName + '\'' + ", email='" + email + '\'' + '}';
        }

    }
