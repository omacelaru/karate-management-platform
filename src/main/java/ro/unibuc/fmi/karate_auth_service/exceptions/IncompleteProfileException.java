package ro.unibuc.fmi.karate_auth_service.exceptions;

public class IncompleteProfileException extends RuntimeException {
    public IncompleteProfileException() {
        super("Profile is incomplete");
    }
}
