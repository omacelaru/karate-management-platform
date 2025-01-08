package ro.unibuc.fmi.karate_auth_service.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
        super("Request not found or it is not in the pending status");
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

    public RequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
