package ua.training.restaurant.exceptions;

public class NotEnoughtFundsException extends Exception {
    public NotEnoughtFundsException() {
    }

    public NotEnoughtFundsException(String message) {
        super(message);
    }
}
