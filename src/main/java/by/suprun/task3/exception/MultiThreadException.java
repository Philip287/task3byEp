package by.suprun.task3.exception;

public class MultiThreadException extends Exception {

    public MultiThreadException() {
    }

    public MultiThreadException(String message) {
        super(message);
    }

    public MultiThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiThreadException(Throwable cause) {
        super(cause);
    }
}
