package ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class KumiteCategory extends Category {
    @Enumerated(EnumType.STRING)
    @Column(name = "kumite_division_range", nullable = false)
    private KumiteDivisionRange kumiteDivisionRange;
}