package ua.training.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.repository.DishRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private DishRepository dishRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository= dishRepository;
    }

    @Override
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public Optional<Dish> findByID(Long id) {
        return dishRepository.findById(id);
    }
}
