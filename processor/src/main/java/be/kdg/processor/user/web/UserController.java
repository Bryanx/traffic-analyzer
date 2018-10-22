package be.kdg.processor.user.web;

import be.kdg.processor.user.User;
import be.kdg.processor.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) throws UserException {
        LOGGER.info("Get request for user with id: {}", id);
        User user = userService.findById(id);
        return new ResponseEntity<>(modelMapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        LOGGER.info("Post request for create user: {}", userDTO);
        User user = modelMapper.map(userDTO, User.class);
        userService.save(user);
        return new ResponseEntity<>(modelMapper.map(user, UserDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userService.save(user);
        return new ResponseEntity<>(modelMapper.map(user,UserDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}
