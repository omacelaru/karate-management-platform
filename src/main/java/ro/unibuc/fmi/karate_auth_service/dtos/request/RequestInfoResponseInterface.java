package ro.unibuc.fmi.karate_auth_service.dtos.request;

import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;

public sealed interface RequestInfoResponseInterface permits RequestWithRolesApprovalResponse, RequestWithUsersApprovalResponse {
    Long getId();

    RequestType getType();

    RequestStatus getStatus();

    UserResponse getCreatedBy();

    Long getLastUpdatedById();
}