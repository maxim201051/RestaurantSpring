package ua.training.restaurant.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.user.Role;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveOrUpdate(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }

    public List<User> findAllUsers() { //means role.user
        return userRepository.findByAuthoritiesContaining(Role.USER);
    }

    @Override
    public User setDefaultParams(User user) {
        List<Role> authorities = new ArrayList<>();
        authorities.add(Role.USER);
        user.setAuthorities(authorities);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setFunds(0L);
        user.setOrdersNumber(0);
        user.setOrdersTotalCost(0L);
        user.setRegistrationDate(LocalDate.now());
        return user;
    }

    @Override
    public User addOrderToStatistic(User user, Order order) {
        user.setFunds(user.getFunds() - order.getAmountTotal());
        user.setOrdersTotalCost(user.getOrdersTotalCost() + order.getAmountTotal());
        user.setOrdersNumber(user.getOrdersNumber() + 1);
        return user;
    }

    @Override
    public User addFunds(User user, Long funds) {
        user.setFunds(user.getFunds() + funds);
        saveOrUpdate(user);
        return user;
    }
}
