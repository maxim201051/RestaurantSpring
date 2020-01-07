package ua.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.service.OrderService;

@Controller
public class HomeController {
    @Autowired
    private OrderService orderService;

    @GetMapping({"/", "/index"})
    public String welcomePage() {
        return "index";
    }

    @GetMapping("/order")
    public ModelAndView orderPage(ModelAndView modelAndView, @RequestParam Long id) {
        modelAndView.addObject("order_units", orderService.findById(id));
        modelAndView.setViewName("order");
        return modelAndView;
    }
}
