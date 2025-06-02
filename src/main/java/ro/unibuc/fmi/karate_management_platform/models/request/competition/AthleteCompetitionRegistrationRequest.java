package ro.unibuc.fmi.karate_management_platform.models.request.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.AthleteRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithUsersApproval;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "athlete_competition_registration_requests")
public class AthleteCompetitionRegistrationRequest extends RequestWithUsersApproval {
    @Column(name = "competition_id", nullable = false)
    private Long competitionId;

    @ElementCollection
    @CollectionTable(
        name = "athlete_competition_registration_individual_categories",
        joinColumns = @JoinColumn(name = "request_id")
    )
    @Column(name = "category_type")
    @Enumerated(EnumType.STRING)
    private Set<IndividualCategoryType> individualCategories = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "athlete_competition_registration_team_categories",
        joinColumns = @JoinColumn(name = "request_id")
    )
    @Column(name = "category_type")
    @Enumerated(EnumType.STRING)
    private Set<TeamCategoryType> teamCategories = new HashSet<>();

    public void prePersist() {
        super.prePersist();
        this.setType(RequestType.ATHLETE_COMPETITION_REGISTRATION);
        this.setStatus(RequestStatus.PENDING);
        this.setScope(RequestScope.USERS);
    }
}
