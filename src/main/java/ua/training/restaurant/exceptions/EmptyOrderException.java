package ua.training.restaurant.exceptions;

/**
 * Created by Student
 */
public class EmptyOrderException extends Exception {
    public EmptyOrderException() {
    }

    public EmptyOrderException(String message) {
        super(message);
    }
}
