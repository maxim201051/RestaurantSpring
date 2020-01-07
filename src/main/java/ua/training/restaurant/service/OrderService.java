package ua.training.restaurant.service;

import org.aspectj.weaver.ast.Or;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.Order_Status;
import ua.training.restaurant.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findByUser(User user);

    Optional<Order> findById(Long id);

    Order save(Order order, User user);

    List<Order> findByStatus(Order_Status order_status);
    Order update(Order order) ;
}
