// app/repo/TaskRepository.java
package app.repo;
import app.model.Task;
import java.util.*;

public interface TaskRepository {
    List<Task> listByUser(long userId) throws Exception;
    long save(Task t) throws Exception;     // INSERT
    void update(Task t) throws Exception;   // UPDATE
    void delete(long id, long userId) throws Exception;
}
