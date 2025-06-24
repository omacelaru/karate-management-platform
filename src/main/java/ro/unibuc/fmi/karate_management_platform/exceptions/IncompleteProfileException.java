package ro.unibuc.fmi.karate_management_platform.exceptions;

public class IncompleteProfileException extends RuntimeException {
    public IncompleteProfileException() {
        super("Profile is incomplete");
    }
}
