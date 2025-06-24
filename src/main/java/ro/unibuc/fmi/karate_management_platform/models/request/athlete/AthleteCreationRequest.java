package ro.unibuc.fmi.karate_management_platform.models.request.athlete;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithUsersApproval;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "athlete_creation_requests")
public class AthleteCreationRequest extends RequestWithUsersApproval {
    @Embedded
    private AthleteRequest athleteRequest;

    public void prePersist() {
        super.prePersist();
        this.setType(RequestType.ATHLETE_CREATION);
        this.setStatus(RequestStatus.PENDING);
        this.setScope(RequestScope.USERS);
    }
}
