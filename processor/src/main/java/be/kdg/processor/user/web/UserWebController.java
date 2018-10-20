package be.kdg.processor.user.web;

import be.kdg.processor.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class UserWebController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/users")
    public ModelAndView getAllUsers() {
        UserDTO[] users = modelMapper.map(userService.findAll().toArray(), UserDTO[].class);
        return new ModelAndView("users", "users", users);
    }
}
