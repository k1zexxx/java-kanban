package exceptions;

public class ManagerSaveException extends Throwable {
    public ManagerSaveException(final String message, final Throwable ex) {
        super(message, ex);
    }
}
