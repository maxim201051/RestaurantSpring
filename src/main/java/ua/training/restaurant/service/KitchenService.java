package ua.training.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.Order_Status;
import ua.training.restaurant.entity.Role;
import ua.training.restaurant.entity.User;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;

//Аля заглушка
@Service
@Slf4j
public class KitchenService {
   public static Order cookDishes(Order order) {
       order.setStatus(Order_Status.READY);
       order.setReady(now());
       return order;
   }
}
