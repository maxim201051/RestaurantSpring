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
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.User;
import ua.training.restaurant.service.DishService;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.UserService;
import ua.training.restaurant.utils.Utils;

import javax.servlet.http.HttpServletRequest;

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
}