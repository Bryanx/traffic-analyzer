package be.kdg.processor.user.web;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.fine.FineService;
import be.kdg.processor.shared.exception.ProcessorException;
import be.kdg.processor.shared.livestats.LiveStatsController;
import be.kdg.processor.user.User;
import be.kdg.processor.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserWebController extends LiveStatsController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserWebController(FineService fineService, CameraService cameraService, ModelMapper modelMapper, UserService userService) {
        super(fineService, cameraService);
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public ModelAndView getAllUsers() {
        UserDTO[] users = modelMapper.map(userService.findAll().toArray(), UserDTO[].class);
        return new ModelAndView("users", "users", users);
    }

    @GetMapping("/users/details/{id}")
    public ModelAndView getUser(@PathVariable int id) throws ProcessorException {
        UserDTO user = modelMapper.map(userService.findById(id), UserDTO.class);
        return new ModelAndView("users/details", "user", user);
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.setViewName("register");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("redirect:/register");
        }
        User userOut = userService.save(modelMapper.map(userDTO, User.class));
        modelAndView.addObject("user", modelMapper.map(userOut, UserDTO.class));
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
