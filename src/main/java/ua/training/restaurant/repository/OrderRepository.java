package ua.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.restaurant.entity.Order;
import ua.training.restaurant.entity.Order_Status;
import ua.training.restaurant.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findById(Long id);
    List<Order> findByStatus(Order_Status order_status);
}