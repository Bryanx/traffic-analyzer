package be.kdg.processor.user;

import be.kdg.processor.user.web.UserNotFoundException;

public interface UserService {
    User save(User user);
    User findById(int id) throws UserNotFoundException;
    void deleteById(int id);
}
