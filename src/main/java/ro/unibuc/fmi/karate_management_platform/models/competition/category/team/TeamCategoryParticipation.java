package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
