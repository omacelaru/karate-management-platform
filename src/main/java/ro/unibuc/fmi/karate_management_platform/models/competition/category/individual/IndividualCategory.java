package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

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
public abstract class IndividualCategory extends Category {
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private IndividualCategoryType categoryType;
}
