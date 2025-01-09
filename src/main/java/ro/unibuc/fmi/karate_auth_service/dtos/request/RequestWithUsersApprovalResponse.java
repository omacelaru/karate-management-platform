package ro.unibuc.fmi.karate_auth_service.dtos.request;

import lombok.Data;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;

import java.util.Set;

@Data
public sealed class RequestWithUsersApprovalResponse implements RequestInfoResponseInterface permits AthleteCreationResponse {
    Long id;
    RequestType type;
    RequestStatus status;
    UserResponse createdBy;
    Long lastUpdatedById;
    Set<UserResponse> approverUsers;
}
