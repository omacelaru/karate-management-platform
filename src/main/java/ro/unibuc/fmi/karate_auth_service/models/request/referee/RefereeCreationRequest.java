package ro.unibuc.fmi.karate_auth_service.models.request.referee;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestWithRolesApproval;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
