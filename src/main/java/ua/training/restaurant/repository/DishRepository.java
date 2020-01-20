package ua.training.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.restaurant.entity.Dish;

import java.util.Optional;

/**
 * Created by Student
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findAll(Pageable pageable);

    Optional<Dish> findById(Long id);
}
