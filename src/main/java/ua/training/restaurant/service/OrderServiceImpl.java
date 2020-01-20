package ua.training.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.entity.OrderUnit;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.EmptyOrderException;
import ua.training.restaurant.exceptions.OrderNotFoundException;
import ua.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

/**
 * Created by Student
 */
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
    public Order findById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(Order order, User user) throws EmptyOrderException {
        if (!order.isEmpty()) {
            order.setUser(user);
            order.setStatus(Order_Status.CREATED);
            order.setCreated(now());
            orderRepository.save(order);
        } else {
            throw new EmptyOrderException();
        }
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

    private Optional<OrderUnit> findOrderUnitById(Order order, Long id) {
        return order.getOrderUnits().stream().filter(unit -> unit.getDish().getId().equals(id)).findFirst();
    }

    public Order addDish(Order order, Dish dish, int quantity) {
        Optional<OrderUnit> unit = this.findOrderUnitById(order, dish.getId());

        if (!unit.isPresent()) {
            unit = Optional.of(new OrderUnit());
            unit.get().setQuantity(0);
            unit.get().setDish(dish);
            order.getOrderUnits().add(unit.get());
        }
        int newQuantity = unit.get().getQuantity() + quantity;
        if (newQuantity <= 0) {
            order.getOrderUnits().remove(unit.get());
        } else {
            unit.get().setQuantity(newQuantity);
        }
        return order;
    }

    public Order updateDish(Order order, Long id, int quantity) {
        Optional<OrderUnit> unit = this.findOrderUnitById(order, id);
        unit.ifPresent(u -> {
            if (quantity <= 0) {
                order.getOrderUnits().remove(unit.get());
            } else {
                unit.get().setQuantity(quantity);
            }
        });
        return order;
    }

    public Order removeDish(Order order, Dish dish) {
        Optional<OrderUnit> unit = this.findOrderUnitById(order, dish.getId());
        unit.ifPresent(u -> order.getOrderUnits().remove(unit.get()));
        return order;
    }

    public Order updateQuantity(Order order1, Order order2) {
        Optional.ofNullable(order2).ifPresent(o->{
            List<OrderUnit> units = order2.getOrderUnits();
            units.forEach(unit -> this.updateDish(order1, unit.getDish().getId(), unit.getQuantity()));
        });
        return order1;
    }

    @Override
    public List<Order> findByUserId(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        makeUserUnique(orders);
        return orders;
    }

    @Override
    public void confirmOrder(Long id) throws OrderNotFoundException {
        Order order = findById(id);
        order.setAccepted(now());
        order.setStatus(Order_Status.ACCEPTED);
        update(order);
        KitchenService.cookDishes(order);
        update(order);
    }

    @Override
    public void setPaid(Order order) {
        order.setStatus(Order_Status.PAID);
        order.setPaid(now());
    }

    private void makeUserUnique(List<Order> orders) {
        User user = orders.get(0).getUser();
        orders.forEach(o -> o.setUser(user));
    }
}
