package flooring.service;

public class OrderDaoDuplicateIdException extends Exception {

        public OrderDaoDuplicateIdException(String message) {
            super(message);
        }

        public OrderDaoDuplicateIdException(String message, Throwable cause) {
            super(message, cause);
    }
}
