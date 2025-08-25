package app.repo;
import app.model.User;
import java.util.*;

public interface UserRepository {
    Optional<User> findByEmail(String email) throws Exception;
    Optional<User> findById(long id) throws Exception;
    List<User> findAll() throws Exception;
    long save(User u) throws Exception;     // INSERT
    void update(User u) throws Exception;   // UPDATE
}
