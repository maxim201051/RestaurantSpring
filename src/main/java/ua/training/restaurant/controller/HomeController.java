package ua.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.restaurant.exceptions.OrderNotFoundException;
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
    public ModelAndView orderPage(ModelAndView modelAndView, @RequestParam Long id, RedirectAttributes redir) {
        try {
            modelAndView.addObject("order", orderService.findById(id));
            modelAndView.setViewName("order");
        } catch (OrderNotFoundException e) {
            redir.addFlashAttribute("failureMessage","order.label.failureMessage");
            modelAndView.setViewName("redirect:/index");
        }
        return modelAndView;
    }
}
