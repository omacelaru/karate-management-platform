package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public abstract class TeamCategory extends Category {
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private TeamCategoryType categoryType;
}
