package ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class KataTeamCategory extends TeamCategory {
    @Enumerated(EnumType.STRING)
    @Column(name = "kata_team_type", nullable = false)
    private KataTeamType kataTeamType;
}
