package ro.unibuc.fmi.karate_auth_service.exceptions;

import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(RequestStatus expectedStatus) {
        super(String.format("Request not found or it is not in the %s status", expectedStatus));
    }
}
