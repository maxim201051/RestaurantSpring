package ua.training.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.exceptions.OrderNotFoundException;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.UserService;

import java.util.List;

/**
 * Created by Student
 */
@Slf4j
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
        log.info("getting userstatistics page");
        modelAndView.addObject("users", userService.findAllUsers());
        modelAndView.setViewName("userstatistics");
        return modelAndView;
    }

    @GetMapping("/concreteuserstatistic")
    public ModelAndView concreteUserStatisticsPage(ModelAndView modelAndView, @RequestParam Long id) {
        log.info("getting concreteuserstatistic page");
        try {
            List<Order> orders = orderService.findByUserId(id);
            modelAndView.addObject("orders", orders);
            modelAndView.setViewName("concreteuserstatistic");
        } catch (Exception e) {
            log.error("cannot find user by id " + id);
            modelAndView.setViewName("redirect:/admin/userstatistics");
        }
        return modelAndView;
    }

    @GetMapping("/orderconfirmation")
    public ModelAndView orderConfirmationPage(ModelAndView modelAndView) {
        log.info("getting orderconfirmation page");
        modelAndView.addObject("orders", orderService.findByStatus(Order_Status.CREATED));
        modelAndView.setViewName("orderconfirmation");
        return modelAndView;
    }

    @GetMapping("/acceptorder")
    public ModelAndView confirmOrder(ModelAndView modelAndView, @RequestParam Long id, RedirectAttributes redir) {
        log.info("trying to confirm order");
        try {
            orderService.confirmOrder(id);
        } catch (OrderNotFoundException e) {
            log.error("cannot find order by id " + id);
            redir.addFlashAttribute("failureMessage", "order.label.failureMessage");
        }
        modelAndView.setViewName("redirect:/admin/orderconfirmation");
        return modelAndView;
    }

    @GetMapping("/billmaking")
    public ModelAndView billMakingPage(ModelAndView modelAndView) {
        log.info("getting billmaking page");
        modelAndView.addObject("orders", orderService.findByStatus(Order_Status.READY));
        modelAndView.setViewName("billmaking");
        return modelAndView;
    }

    @GetMapping("/makebill")
    public ModelAndView makeBill(ModelAndView modelAndView, @RequestParam Long id, RedirectAttributes redir) {
        log.info("trying to make bill");
        Order order;
        try {
            order = orderService.findById(id);
            order.setStatus(Order_Status.UNPAID);
            orderService.update(order);
        } catch (OrderNotFoundException e) {
            log.error("cannot find order by id " + id);
            redir.addFlashAttribute("failureMessage", "order.label.failureMessage");
        }
        modelAndView.setViewName("redirect:/admin/billmaking");
        return modelAndView;
    }
}
