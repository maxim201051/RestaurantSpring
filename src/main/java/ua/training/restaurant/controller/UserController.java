package ua.training.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.restaurant.entity.Bill;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.DishNotFoundException;
import ua.training.restaurant.exceptions.EmptyOrderException;
import ua.training.restaurant.exceptions.OrderNotFoundException;
import ua.training.restaurant.service.DishService;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.UserService;
import ua.training.restaurant.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student
 */
@Slf4j
@Controller
@RequestMapping("/user/*")
public class UserController {
    private static final int NUMBER_OF_DISHES_ON_PAGE = 10;
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String userPage() {
        log.info("getting user page");
        return "user";
    }

    @GetMapping("/addfunds")
    public ModelAndView addFundsPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        log.info("getting addfunds page");
        modelAndView.addObject(user);
        modelAndView.setViewName("addfunds");
        return modelAndView;
    }

    @PostMapping("/addfunds")
    public ModelAndView addFunds(ModelAndView modelAndView, @AuthenticationPrincipal User user, Long funds, RedirectAttributes redir) {
        log.info("trying to add finds to user account");
        try {
            Utils.throwExIfFundsNotValid(funds);
            userService.addFunds(user, funds);
            modelAndView.setViewName("redirect:/user/");
        } catch (Exception e) {
            log.error("invalid funds number");
            redir.addFlashAttribute("failureMessage", "addfunds.label.error");
            modelAndView.setViewName("redirect:/user/addfunds");
        }
        return modelAndView;
    }

    @GetMapping("/concreteuserstatistic")
    public ModelAndView concreteUserStatisticsPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        log.info("getting concreteuserstatistics page");
        modelAndView.addObject("orders", orderService.findByUser(user));
        modelAndView.setViewName("concreteuserstatistic");
        return modelAndView;
    }

    @GetMapping("/foodmenu")
    public ModelAndView foodMenuPage(ModelAndView modelAndView, @RequestParam Integer page) {
        log.info("getting foodmenu page number " + page);
        Pageable pageable = PageRequest.of(page - 1, NUMBER_OF_DISHES_ON_PAGE);
        modelAndView.addObject("dishes", dishService.findAll(pageable));
        modelAndView.setViewName("foodmenu");
        return modelAndView;
    }

    @GetMapping("/buyproduct")
    public ModelAndView addDishToOrder(ModelAndView modelAndView, @RequestParam Long id, HttpServletRequest request,
                                       RedirectAttributes redir) {
        log.info("trying to add dish with id " + id + " to shopping cart");
        Dish dish;
        try {
            dish = dishService.findByID(id);
            Order order = Utils.getOrderInSession(request);
            orderService.addDish(order, dish, 1);
        } catch (DishNotFoundException e) {
            log.error("cannot find dish by id " + id);
            redir.addFlashAttribute("failureMessage", "dish.label.failureMessage");
        }
        modelAndView.setViewName("redirect:/user/foodmenu?page=1");
        return modelAndView;
    }

    @GetMapping("/shoppingcart")
    public ModelAndView shoppingCartPage(ModelAndView modelAndView, HttpServletRequest request) {
        log.info("getting shoppingcart page");
        modelAndView.addObject("order", Utils.getOrderInSession(request));
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }

    @GetMapping("/shoppingcartremovedish")
    public ModelAndView shoppingCartRemoveDish(ModelAndView modelAndView, @RequestParam Long id, HttpServletRequest request,
                                               RedirectAttributes redir) {
        log.info("trying to remove dish with id " + id);
        Dish dish;
        try {
            dish = dishService.findByID(id);
            Order order = Utils.getOrderInSession(request);
            orderService.removeDish(order, dish);
        } catch (DishNotFoundException e) {
            log.error("cannot find dish by id " + id);
            redir.addFlashAttribute("failureMessage", "dish.label.failureMessage");
        }
        modelAndView.setViewName("redirect:/user/shoppingcart");
        return modelAndView;
    }

    @PostMapping("/shoppingcart")
    public ModelAndView shoppingCartUpdateQuantity(ModelAndView modelAndView, HttpServletRequest request, Order orderForm) {
        log.info("trying to update dish quantity");
        Order order = Utils.getOrderInSession(request);
        orderService.updateQuantity(order, orderForm);
        return modelAndView;
    }

    @PostMapping("/saveorder")
    public String saveOrderToDB(@AuthenticationPrincipal User user, HttpServletRequest request, RedirectAttributes redir) {
        log.info("trying to insert created order into db");
        Order order = Utils.getOrderInSession(request);
        try {
            orderService.save(order, user);
            Utils.removeOrderInSession(request);
        } catch (EmptyOrderException e) {
            log.error("order is empty");
            redir.addFlashAttribute("failureMessage", "shoppingcart.label.failureMessage");
        }
        return "redirect:/user/shoppingcart";
    }

    @GetMapping("/billpaying")
    public ModelAndView billPayingPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        log.info("getting billpaying page");
        List<Order> orders = orderService.findByUser(user);
        List<Bill> bills = new ArrayList<>();
        orders.stream().filter(x -> x.getStatus().equals(Order_Status.UNPAID)).forEach(x -> bills.add(new Bill(x)));
        modelAndView.addObject("bills", bills);
        modelAndView.setViewName("billpaying");
        return modelAndView;
    }

    @GetMapping("/paybill")
    public ModelAndView payBill(ModelAndView modelAndView, @AuthenticationPrincipal User user, @RequestParam Long id,
                                RedirectAttributes redir) {
        log.info("trying to pay bill");
        Order order;
        try {
            order = orderService.findById(id);
            userService.addOrderToStatistic(user, order);
            orderService.setPaid(order);
            saveUserAndOrder(user, order);
        } catch (OrderNotFoundException e) {
            log.error("cannot find order by id " + id);
            redir.addFlashAttribute("failureMessage", "order.label.failureMessage");
        } catch (Exception e) {
            log.error("user dont have enough funds");
            redir.addFlashAttribute("failureMessage", "paying.label.notEnoughFunds");
        }
        modelAndView.setViewName("redirect:/user/billpaying");
        return modelAndView;
    }

    @Transactional
    public boolean saveUserAndOrder(User user, Order order) {
        userService.saveOrUpdate(user);
        orderService.update(order);
        return true;
    }
}