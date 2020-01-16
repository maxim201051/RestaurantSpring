package ua.training.restaurant.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.NotEnoughtFundsException;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> listAll();

    User getById(Long id);

    User saveOrUpdate(User user);

    boolean isUserExists(String username);

    List<User> findAllUsers();

    User setDefaultParams(User user);

    void addOrderToStatistic(User user, Order order) throws NotEnoughtFundsException;
    void addFunds(User user, Long funds);
}
