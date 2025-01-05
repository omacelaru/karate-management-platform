package ro.unibuc.fmi.karate_auth_service.dtos.request;

import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Set;


public sealed interface RequestWithRolesApprovalResponseInterface extends RequestInfoResponseInterface permits CoachCreationResponse {
    Set<Role> approverRoles();
}