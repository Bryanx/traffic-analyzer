package be.kdg.processor.user;

public interface UserService {
    User save(User user);
    User findById(int id) throws UserNotFoundException;
    void deleteById(int id);
}
