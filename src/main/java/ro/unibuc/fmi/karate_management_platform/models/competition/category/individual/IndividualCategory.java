package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public abstract class IndividualCategory extends Category {
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<IndividualCategoryParticipation> participations = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private IndividualCategoryType categoryType;

    public abstract void calculateDuration();
}
