package ua.training.restaurant.exceptions;

public class DishNotFoundException extends Exception {
    public DishNotFoundException() {
    }

    public DishNotFoundException(String message) {
        super(message);
    }
}
