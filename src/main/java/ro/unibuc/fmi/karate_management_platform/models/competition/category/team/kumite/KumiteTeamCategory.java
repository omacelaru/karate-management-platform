package ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class KumiteTeamCategory extends TeamCategory {
    @Enumerated(EnumType.STRING)
    @Column(name = "kumite_team_type", nullable = false)
    private KumiteTeamType kumiteTeamType;

    @Override
    public void calculateDuration() {
        getParticipations().stream()
                .filter(participation -> participation.getCategory() instanceof KumiteTeamCategory)
                .forEach(participation -> {
                    participation.setDurationMinutes(4); // Each kata performance average 4 minutes
                });
    }
}
