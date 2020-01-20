package ua.training.restaurant.exceptions;

/**
 * Created by Student
 */
public class DishNotFoundException extends Exception {
    public DishNotFoundException() {
    }

    public DishNotFoundException(String message) {
        super(message);
    }
}
