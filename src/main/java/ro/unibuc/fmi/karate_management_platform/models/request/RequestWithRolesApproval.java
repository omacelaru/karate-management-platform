package ro.unibuc.fmi.karate_management_platform.models.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class RequestWithRolesApproval extends RequestInfo {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "request_approver_roles",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id", table = "request_info")
    )
    @Column(name = "approver_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> approverRoles = new LinkedHashSet<>();
}
