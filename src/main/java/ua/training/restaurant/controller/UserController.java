package ua.training.restaurant.controller;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.restaurant.entity.*;
import ua.training.restaurant.service.DishService;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.UserService;
import ua.training.restaurant.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Controller
@RequestMapping("/user/*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String userPage() {
        return "user";
    }

    @GetMapping("/addfunds")
    public ModelAndView addFundsPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        modelAndView.addObject(user);
        modelAndView.setViewName("addfunds");
        return modelAndView;
    }

    @PostMapping("/addfunds")
    public ModelAndView addFunds(ModelAndView modelAndView, @AuthenticationPrincipal User user, Long funds, RedirectAttributes redir) {
        if (UtilityController.checkFundsToAdd(funds)) {
            user.setFunds(user.getFunds() + funds);
            userService.saveOrUpdate(user);
            redir.addFlashAttribute("successMessage", "=)");
            modelAndView.setViewName("redirect:/user/");
        } else {
            redir.addFlashAttribute("unsuccessfulMessage", "=(");
            modelAndView.setViewName("redirect:/user/");
        }
        return modelAndView;
    }

    @GetMapping("/concreteuserstatistic")
    public ModelAndView concreteUserStatisticsPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        modelAndView.addObject("user", user);
        modelAndView.addObject("orders", orderService.findByUser(user));
        modelAndView.setViewName("concreteuserstatistic");
        return modelAndView;
    }

    @GetMapping("/foodmenu")
    public ModelAndView foodMenuPage(ModelAndView modelAndView) {
        modelAndView.addObject("dishes", dishService.findAll());
        modelAndView.setViewName("foodmenu");
        return modelAndView;
    }

    @GetMapping("/buyproduct")
    public String addDishToOrder(@RequestParam Long id, HttpServletRequest request) {
        Dish dish = null;
        if (id != null) {
            dish = dishService.findByID(id).get();
        }
        if (dish != null) {
            //
            Order order = Utils.getOrderInSession(request);
            order.addDish(dish, 1);
        }
        return "redirect:/user/foodmenu";
    }
    @GetMapping("/shoppingcart")
    public ModelAndView shoppingCartPage(ModelAndView modelAndView,HttpServletRequest request) {
        modelAndView.addObject("order",Utils.getOrderInSession(request));
        modelAndView.setViewName("shoppingcart");
        return modelAndView;
    }
    @GetMapping("/shoppingcartremovedish")
    public String shoppingCartRemoveDish(@RequestParam Long id,HttpServletRequest request) {
        Dish dish = null;
        if (id != null) {
            dish = dishService.findByID(id).get();
            //dish = dishService.findByID(id).orElseThrow();
        }
        if (dish != null) {
            Order order = Utils.getOrderInSession(request);
            order.removeDish(dish);
        }
        return "redirect:/user/shoppingcart";
    }
    @PostMapping("/shoppingcart")
    public ModelAndView shoppingCartUpdateQuantity(ModelAndView modelAndView,HttpServletRequest request, Order orderForm) {
        Order order = Utils.getOrderInSession(request);
        order.updateQuantity(orderForm);
        return modelAndView;
    }
    @PostMapping("/saveorder")
    public ModelAndView saveOrderToDB(ModelAndView modelAndView, Order order, @AuthenticationPrincipal User user) {
        orderService.save(order,user);
        return modelAndView;
    }
    @GetMapping("/billpaying")
    public ModelAndView billPayingPage(ModelAndView modelAndView, @AuthenticationPrincipal User user) {
        List<Order> orders = orderService.findByUser(user);
        List<Bill> bills = new ArrayList<>();
        orders.stream().filter(x->x.getStatus().equals(Order_Status.UNPAID)).forEach(x->bills.add(new Bill(x)));
        modelAndView.addObject("bills",bills);
        modelAndView.setViewName("billpaying");
        return modelAndView;
    }
    @GetMapping("/paybill")
    public String payBill(@AuthenticationPrincipal User user, @RequestParam Long id) {
        Order order = orderService.findById(id).get();
        //Order order = orderService.findById(id).orElseThrow();
        if(order.getAmountTotal()>user.getFunds()) {

        } else {
            user.setFunds(user.getFunds()-order.getAmountTotal());
            user.setOrdersTotalCost(user.getOrdersTotalCost()+order.getAmountTotal());
            user.setOrdersNumber(user.getOrdersNumber()+1);
            userService.saveOrUpdate(user);
            order.setStatus(Order_Status.PAID);
            order.setPaid(now());
            orderService.update(order);
        }
        return "redirect:/user/billpaying";
    }
}