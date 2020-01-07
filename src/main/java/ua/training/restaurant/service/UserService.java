package ua.training.restaurant.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.training.restaurant.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> listAll();

    User getById(Long id);

    User saveOrUpdate(User user);

    boolean isUserExists(String username);

    List<User> findAllUsers();

    User setDefaultParams(User user);
}
