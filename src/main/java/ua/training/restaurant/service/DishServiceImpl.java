package ua.training.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.exceptions.DishNotFoundException;
import ua.training.restaurant.repository.DishRepository;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private DishRepository dishRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public Page<Dish> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable);
    }

    @Override
    public Dish findByID(Long id) throws DishNotFoundException {
        return dishRepository.findById(id).orElseThrow(DishNotFoundException::new);
    }
}
