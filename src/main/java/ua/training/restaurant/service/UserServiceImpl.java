package ua.training.restaurant.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.training.restaurant.controller.UtilityController;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.entity.user.Role;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.NotEnoughtFundsException;
import ua.training.restaurant.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveOrUpdate(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
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
    public void addOrderToStatistic(User user, Order order) throws NotEnoughtFundsException {
        if (order.getAmountTotal() > user.getFunds()) {
            throw new NotEnoughtFundsException();
        } else {
            user.setFunds(user.getFunds() - order.getAmountTotal());
            user.setOrdersTotalCost(user.getOrdersTotalCost() + order.getAmountTotal());
            user.setOrdersNumber(user.getOrdersNumber() + 1);

        }
    }

    @Override
    public void addFunds(User user, Long funds) {
        if (UtilityController.checkFundsToAdd(funds)) {
            user.setFunds(user.getFunds() + funds);
            saveOrUpdate(user);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
