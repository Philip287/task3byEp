package by.suprun.task3.exception;

public class MultithreadingException extends Exception {

    public MultithreadingException() {
    }

    public MultithreadingException(String message) {
        super(message);
    }


    public MultithreadingException(String message, Throwable cause) {
        super(message, cause);
    }


    public MultithreadingException(Throwable cause) {
        super(cause);
    }
}
