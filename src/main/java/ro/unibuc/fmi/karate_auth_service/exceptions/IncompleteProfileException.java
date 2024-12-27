package ro.unibuc.fmi.karate_auth_service.exceptions;

import org.apache.coyote.BadRequestException;

public class IncompleteProfileException extends BadRequestException {
    public IncompleteProfileException() {
        super("Profile is incomplete");
    }
}
