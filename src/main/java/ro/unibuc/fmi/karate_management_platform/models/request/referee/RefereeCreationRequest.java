package ro.unibuc.fmi.karate_management_platform.models.request.referee;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithRolesApproval;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "referee_creation_requests")
public class RefereeCreationRequest extends RequestWithRolesApproval {
    @Embedded
    private RefereeRequest refereeRequest;

    public void prePersist() {
        super.prePersist();
        this.setType(RequestType.REFEREE_CREATION);
        this.setStatus(RequestStatus.PENDING);
        this.setScope(RequestScope.ROLES);
    }
}
