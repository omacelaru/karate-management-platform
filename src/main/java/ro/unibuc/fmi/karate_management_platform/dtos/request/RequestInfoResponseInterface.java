package ro.unibuc.fmi.karate_management_platform.dtos.request;

import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;

import java.time.LocalDateTime;

public sealed interface RequestInfoResponseInterface permits RequestWithRolesApprovalResponse, RequestWithUsersApprovalResponse {
    Long getId();

    RequestType getType();

    RequestStatus getStatus();

    UserResponse getCreatedBy();

    Long getLastUpdatedById();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();


}