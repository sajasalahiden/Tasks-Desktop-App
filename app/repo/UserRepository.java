package app.repo;
import app.model.User;
import java.util.*;

public interface UserRepository {
    Optional<User> findByEmail(String email) throws Exception;
    Optional<User> findById(long id) throws Exception;
    List<User> findAll() throws Exception;
    long save(User u) throws Exception;
    void update(User u) throws Exception;
    void updatePasswordById(long userId, String newHash)throws Exception;
    void updatePasswordByEmail(String email, String newHash)throws Exception;

}
