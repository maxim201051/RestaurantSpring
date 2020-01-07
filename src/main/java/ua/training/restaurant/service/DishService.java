package ua.training.restaurant.service;

import ua.training.restaurant.entity.Dish;

import java.util.List;
import java.util.Optional;

public interface DishService {
    List<Dish> findAll();
    Optional<Dish> findByID(Long id);
}
