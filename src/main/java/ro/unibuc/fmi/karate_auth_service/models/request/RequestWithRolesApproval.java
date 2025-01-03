package ro.unibuc.fmi.karate_auth_service.models.request;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
public abstract class RequestWithRolesApproval extends RequestInfo {
    @Column(name = "approver_roles", nullable = false)
    private Set<Role> approverRoles;
}
