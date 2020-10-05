package exceptions;

public class EventException extends Exception {

    public EventException(Throwable throwable) {
        super(throwable);
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
