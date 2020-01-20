package ua.training.restaurant.exceptions;

/**
 * Created by Student
 */
public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException() {
    }
}
