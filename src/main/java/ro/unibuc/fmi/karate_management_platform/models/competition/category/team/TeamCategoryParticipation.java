package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryParticipation;

@Entity
@Table(name = "team_category_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeamCategoryParticipation extends CategoryParticipation {

    @ManyToOne
    private TeamCategory category;

    @ManyToOne
    private Team team;
}
