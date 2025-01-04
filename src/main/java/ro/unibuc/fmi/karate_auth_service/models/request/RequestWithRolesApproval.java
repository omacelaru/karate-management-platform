package ro.unibuc.fmi.karate_auth_service.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
@AllArgsConstructor
public abstract class RequestWithRolesApproval extends RequestInfo {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "request_approver_roles", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "approver_roles", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> approverRoles = new LinkedHashSet<>();
}
