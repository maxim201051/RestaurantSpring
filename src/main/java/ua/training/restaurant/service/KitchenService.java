package ua.training.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;

import static java.time.LocalDateTime.now;

/**
 * Created by Student
 */
@Service
@Slf4j
public class KitchenService {
    public static Order cookDishes(Order order) {
        log.info("Order is cooking");
        order.setStatus(Order_Status.READY);
        order.setReady(now());
        return order;
    }
}
