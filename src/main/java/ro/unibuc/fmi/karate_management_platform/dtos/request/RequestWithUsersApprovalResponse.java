package ro.unibuc.fmi.karate_management_platform.dtos.request;

import lombok.Data;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public sealed class RequestWithUsersApprovalResponse implements RequestInfoResponseInterface permits AthleteCreationResponse {
    Long id;
    RequestType type;
    RequestStatus status;
    UserResponse createdBy;
    Long lastUpdatedById;
    Set<UserResponse> approverUsers;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
