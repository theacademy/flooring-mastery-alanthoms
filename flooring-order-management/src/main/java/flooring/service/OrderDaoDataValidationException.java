package flooring.service;

public class OrderDaoDataValidationException extends Exception {
    public OrderDaoDataValidationException(String message) {
        super(message);
    }

    public OrderDaoDataValidationException(String message,
                                              Throwable cause) {
        super(message, cause);
    }
}
