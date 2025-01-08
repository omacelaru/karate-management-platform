package ro.unibuc.fmi.karate_auth_service.dtos.request;

import lombok.Data;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Set;

@Data
public sealed class RequestWithRolesApprovalResponse implements RequestInfoResponseInterface permits CoachCreationResponse, RefereeCreationResponse {
    Long id;
    RequestType type;
    RequestStatus status;
    UserResponse createdBy;
    Long lastUpdatedById;
    Set<Role> approverRoles;
}