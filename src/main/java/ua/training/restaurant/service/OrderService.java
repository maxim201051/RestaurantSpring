package ua.training.restaurant.service;

import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.EmptyOrderException;
import ua.training.restaurant.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderService {
    List<Order> findByUser(User user);

    Order findById(Long id) throws OrderNotFoundException;

    Order save(Order order, User user) throws EmptyOrderException;

    List<Order> findByStatus(Order_Status order_status);

    Order update(Order order);

    Order addDish(Order order, Dish dish, int quantity);

    Order updateDish(Order order, Long id, int quantity);

    Order removeDish(Order order, Dish dish);

    Order updateQuantity(Order order1, Order order2);

    List<Order> findByUserId(Long id);

    void confirmOrder(Long id) throws OrderNotFoundException;

    void setPaid(Order order);
}
