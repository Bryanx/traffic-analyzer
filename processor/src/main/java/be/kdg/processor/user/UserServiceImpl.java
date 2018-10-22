package be.kdg.processor.user;

import be.kdg.processor.user.roles.Role;
import be.kdg.processor.user.roles.RoleRepository;
import be.kdg.processor.user.roles.RoleType;
import be.kdg.processor.user.web.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword()));
        Role role = roleRepository.findFirstByRoleType(RoleType.ROLE_ADMIN);
        user.getRoles().add(role);
        role.getUsers().add(user);
        return userRepository.save(user);
    }

    @Override
    public User findById(int id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found."));
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public void checkLogin(Integer userId, String currentPassword) throws UserException {
        User u = findById(userId);

        if (u == null || !passwordEncoder.matches(currentPassword, u.getEncryptedPassword())) {
            throw new UserException(("Username or password is wrong for id: " + userId));
        }
    }
}
