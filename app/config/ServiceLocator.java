package app.config;

import app.repo.TaskRepository;
import app.repo.UserRepository;
import app.repo.mysql.MySqlTaskRepository;
import app.repo.mysql.MySqlUserRepository;

public final class ServiceLocator {
    private static final UserRepository USERS = new MySqlUserRepository();
    private static final TaskRepository TASKS = new MySqlTaskRepository();

    public static UserRepository users() { return USERS; }
    public static TaskRepository tasks() { return TASKS; }

    private ServiceLocator() {} // منع
}
