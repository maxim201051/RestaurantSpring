package ua.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.service.UserService;

import javax.validation.Valid;

@Controller
public class SecurityController {
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {

        modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");

        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult) {
        if (userService.isUserExists(user.getUsername())) {
            modelAndView.addObject("failureMessage","signup.label.alreadyRegistered");
            modelAndView.setViewName("signup");
            bindingResult.reject("username");
        }
        if (bindingResult.hasErrors() || !UtilityController.checkUserFieldsWithRegex(user)) {
            modelAndView.addObject("failureMessage","signup.label.error");
            modelAndView.setViewName("signup");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.setDefaultParams(user);
            userService.saveOrUpdate(user);
            modelAndView.setViewName("signup");
        }
        return modelAndView;
    }
}
