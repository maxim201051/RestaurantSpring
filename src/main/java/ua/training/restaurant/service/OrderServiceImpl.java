package ua.training.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.Order_Status;
import ua.training.restaurant.entity.User;
import ua.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order, User user) {
        order.setUser(user);
        order.setStatus(Order_Status.CREATED);
        order.setCreated(now());
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findByStatus(Order_Status order_status) {
        return orderRepository.findByStatus(order_status);
    }

    @Override
    public Order update(Order order) {
        orderRepository.save(order);
        return order;
    }

}
