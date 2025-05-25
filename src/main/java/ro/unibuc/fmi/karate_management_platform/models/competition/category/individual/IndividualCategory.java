package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class IndividualCategory extends Category {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "individual_category_athletes",
            joinColumns =
            @JoinColumn(name = "individual_category_id", referencedColumnName = "id", table = "category"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id"))
    @ToString.Exclude
    @Column(name = "athletes", nullable = false)
    private Set<Athlete> athletes = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private IndividualCategoryType categoryType;
}
