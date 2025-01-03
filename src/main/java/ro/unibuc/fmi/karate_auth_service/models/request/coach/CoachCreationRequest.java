package ro.unibuc.fmi.karate_auth_service.models.request.coach;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestWithRolesApproval;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "coach_creation_requests")
public class CoachCreationRequest extends RequestWithRolesApproval {
    @Embedded
    private LicenseInfo licenseInfo;

    public void prePersist() {
        super.prePersist();
        this.setType(RequestType.COACH_REQUEST);
        this.setStatus(RequestStatus.PENDING);
    }
}
