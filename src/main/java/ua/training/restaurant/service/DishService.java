package ua.training.restaurant.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.exceptions.DishNotFoundException;

public interface DishService {
    Page<Dish> findAll(Pageable pageable);

    Dish findByID(Long id) throws DishNotFoundException;
}
