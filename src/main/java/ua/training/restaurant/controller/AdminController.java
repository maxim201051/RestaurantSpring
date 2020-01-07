package ua.training.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.Order_Status;
import ua.training.restaurant.entity.User;
import ua.training.restaurant.service.KitchenService;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.UserService;

import static java.time.LocalDateTime.now;

@Controller
@RequestMapping("/admin/*")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/userstatistics")
    public ModelAndView userStatisticsPage(ModelAndView modelAndView) {
        modelAndView.addObject("users", userService.findAllUsers());
        modelAndView.setViewName("userstatistics");
        return modelAndView;
    }

    @GetMapping("/concreteuserstatistic")
    public ModelAndView concreteUserStatisticsPage(ModelAndView modelAndView, @RequestParam Long id) {
        User user = userService.getById(id);
        modelAndView.addObject("user", user);
        modelAndView.addObject("orders", orderService.findByUser(user));
        modelAndView.setViewName("concreteuserstatistic");
        return modelAndView;
    }
    @GetMapping("/orderconfirmation")
    public ModelAndView orderConfirmationPage(ModelAndView modelAndView) {
        modelAndView.addObject("orders",orderService.findByStatus(Order_Status.CREATED));
        modelAndView.setViewName("orderconfirmation");
        return modelAndView;
    }
    @GetMapping("/acceptorder")
    public String confirmOrder(@RequestParam Long id) {
        Order order = orderService.findById(id).get();
        order.setAccepted(now());
        order.setStatus(Order_Status.ACCEPTED);
        orderService.update(order);
        KitchenService.cookDishes(order);
        orderService.update(order);
        return "redirect:/admin/orderconfirmation";
    }
    @GetMapping("/billmaking")
    public ModelAndView billMakingPage(ModelAndView modelAndView) {
        modelAndView.addObject("orders",orderService.findByStatus(Order_Status.READY));
        modelAndView.setViewName("billmaking");
        return modelAndView;
    }
    @GetMapping("/makebill")
    public String makeBill(@RequestParam Long id) {
        Order order = orderService.findById(id).get();
        order.setStatus(Order_Status.UNPAID);
        orderService.update(order);
        return "redirect:/admin/orderconfirmation";
    }
}
