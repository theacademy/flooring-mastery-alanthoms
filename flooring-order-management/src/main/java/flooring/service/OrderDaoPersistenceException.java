package flooring.service;

public class OrderDaoPersistenceException extends Exception {
    public OrderDaoPersistenceException(String message) {
        super(message);
    }

    public OrderDaoPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
