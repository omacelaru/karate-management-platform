package ro.unibuc.fmi.karate_management_platform.dtos.request;

import lombok.Data;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public sealed class RequestWithRolesApprovalResponse implements RequestInfoResponseInterface permits CoachCreationResponse, OrganizerCreationResponse, RefereeCreationResponse, CompetitionCreationResponse {
    Long id;
    RequestType type;
    RequestStatus status;
    UserResponse createdBy;
    Long lastUpdatedById;
    Set<Role> approverRoles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}