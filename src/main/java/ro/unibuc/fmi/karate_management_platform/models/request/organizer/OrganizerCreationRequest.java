package ro.unibuc.fmi.karate_management_platform.models.request.organizer;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithRolesApproval;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "organizer_creation_requests")
public class OrganizerCreationRequest extends RequestWithRolesApproval {
    @Embedded
    private OrganizerRequest organizerRequest;

    public void prePersist() {
        super.prePersist();
        this.setType(RequestType.ORGANIZER_CREATION);
        this.setStatus(RequestStatus.PENDING);
        this.setScope(RequestScope.ROLES);
    }
}
