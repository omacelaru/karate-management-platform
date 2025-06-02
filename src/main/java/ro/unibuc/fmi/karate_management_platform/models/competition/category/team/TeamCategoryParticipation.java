package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;
import jakarta.persistence.*;
import lombok.*;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

@Entity
@Table(name = "team_category_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCategoryParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TeamCategory category;

    @ManyToOne
    private Competition competition;

    @ManyToOne
    private Team team;
}
