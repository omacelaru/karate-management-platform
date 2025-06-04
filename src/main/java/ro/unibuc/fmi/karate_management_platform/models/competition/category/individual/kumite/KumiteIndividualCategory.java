package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class KumiteIndividualCategory extends IndividualCategory {
    @Enumerated(EnumType.STRING)
    @Column(name = "kumite_division_range", nullable = false)
    private KumiteDivisionRange kumiteDivisionRange;

    @Override
    public void calculateDuration() {
        getParticipations().stream()
                .filter(participation -> participation.getCategory() instanceof KumiteIndividualCategory)
                .forEach(participation -> {
                    participation.setDurationMinutes(4); // Each kumite match average 4 minutes
                });
    }
}