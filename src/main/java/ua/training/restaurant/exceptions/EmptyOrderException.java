package ua.training.restaurant.exceptions;

public class EmptyOrderException extends Exception {
    public EmptyOrderException() {
    }

    public EmptyOrderException(String message) {
        super(message);
    }
}
