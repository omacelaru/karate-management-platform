package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
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
public class TeamCategory extends Category {
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<TeamCategoryParticipation> participations = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private TeamCategoryType categoryType;
}
