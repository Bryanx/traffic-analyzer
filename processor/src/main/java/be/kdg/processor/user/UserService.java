package be.kdg.processor.user;

import be.kdg.processor.shared.exception.ProcessorException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(User user);

    User findById(int id) throws ProcessorException;

    void deleteById(int id);

    List<User> findAll();

}
