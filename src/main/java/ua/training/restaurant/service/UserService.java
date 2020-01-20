package ua.training.restaurant.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.user.User;

import java.util.List;

/**
 * Created by Student
 */
public interface UserService extends UserDetailsService {
    User saveOrUpdate(User user);

    List<User> findAllUsers();

    User setDefaultParams(User user);

    User addOrderToStatistic(User user, Order order);

    User addFunds(User user, Long funds);
}
