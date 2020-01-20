package ua.training.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.training.restaurant.exceptions.OrderNotFoundException;
import ua.training.restaurant.service.OrderService;

/**
 * Created by Student
 */
@Slf4j
@Controller
public class HomeController {
    @Autowired
    private OrderService orderService;

    @GetMapping({"/", "/index"})
    public String welcomePage() {
        log.info("getting index page");
        return "index";
    }

    @GetMapping("/order")
    public ModelAndView orderPage(ModelAndView modelAndView, @RequestParam Long id) {
        log.info("getting userstatistics page");
        try {
            modelAndView.addObject("order", orderService.findById(id));
            modelAndView.setViewName("order");
        } catch (OrderNotFoundException e) {
            log.error("cannot find order by id " + id);
            modelAndView.setViewName("redirect:/index");
        }
        return modelAndView;
    }

    @GetMapping("/error")
    public String error404Page() {
        log.info("getting error page");
        return "error";
    }
}
