package ua.training.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Created by Student
 */
@Slf4j
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
        log.info("getting login page");
        return "login";
    }

    @GetMapping("/signup")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        log.info("getting signup page");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");

        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult result) {
        log.info("trying to register new user");
        if (result.hasErrors()) {
            log.error("Invalid data");
            modelAndView.addObject("failureMessage", "signup.label.error");
            modelAndView.setViewName("signup");
        } else {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.setDefaultParams(user);
                userService.saveOrUpdate(user);
                modelAndView.setViewName("redirect:/login");
            } catch (Exception e) {
                log.error("username already exists");
                modelAndView.addObject("failureMessage", "signup.label.alreadyRegistered");
                modelAndView.setViewName("signup");
            }
        }
        return modelAndView;
    }
}
