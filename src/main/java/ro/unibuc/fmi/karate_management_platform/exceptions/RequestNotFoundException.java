package ro.unibuc.fmi.karate_management_platform.exceptions;

import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;

import java.util.Set;

public class RequestNotFoundException extends IllegalArgumentException {

    public RequestNotFoundException(Long requestId) {
        super(String.format("Request with ID %d not found.", requestId));
    }

    public RequestNotFoundException(Long requestId, Set<RequestStatus> expectedStatuses) {
        super(buildMessage(requestId, expectedStatuses));
    }

    private static String buildMessage(Long requestId, Set<RequestStatus> expectedStatuses) {
        String statusPart = expectedStatuses.size() == 1
                ? String.format("status %s", expectedStatuses.iterator().next())
                : String.format("one of the following statuses: %s", expectedStatuses);

        return requestId != null
                ? String.format("Request with ID %d not found or it is not in %s.", requestId, statusPart)
                : String.format("Request not found or it is not in %s.", statusPart);
    }
}
