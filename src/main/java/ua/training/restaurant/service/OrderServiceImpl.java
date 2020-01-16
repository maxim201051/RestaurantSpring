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
        if(!order.isEmpty()) {
            order.setUser(user);
            order.setStatus(Order_Status.CREATED);
            order.setCreated(now());
            orderRepository.save(order);
        }else {
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

    private OrderUnit findOrderUnitById(Order order, Long id) {
        for (OrderUnit unit : order.getOrderUnits()) {
            if (unit.getDish().getId().equals(id)) {
                return unit;
            }
        }
        return null;
    }

    public Order addDish(Order order, Dish dish, int quantity) {
        OrderUnit unit = this.findOrderUnitById(order, dish.getId());

        if (unit == null) {
            unit = new OrderUnit();
            unit.setQuantity(0);
            unit.setDish(dish);
            order.getOrderUnits().add(unit);
        }
        int newQuantity = unit.getQuantity() + quantity;
        if (newQuantity <= 0) {
            order.getOrderUnits().remove(unit);
        } else {
            unit.setQuantity(newQuantity);
        }
        return order;
    }

    public Order updateDish(Order order, Long id, int quantity) {
        OrderUnit unit = this.findOrderUnitById(order, id);

        if (unit != null) {
            if (quantity <= 0) {
                order.getOrderUnits().remove(unit);
            } else {
                unit.setQuantity(quantity);
            }
        }
        return order;
    }

    public Order removeDish(Order order, Dish dish) {
        OrderUnit unit = this.findOrderUnitById(order, dish.getId());
        if (unit != null) {
            order.getOrderUnits().remove(unit);
        }
        return order;
    }

    public Order updateQuantity(Order order1, Order order2) {
        if (order2 != null) {
            List<OrderUnit> units = order2.getOrderUnits();
            units.forEach(unit->this.updateDish(order1, unit.getDish().getId(), unit.getQuantity()));
        }
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
