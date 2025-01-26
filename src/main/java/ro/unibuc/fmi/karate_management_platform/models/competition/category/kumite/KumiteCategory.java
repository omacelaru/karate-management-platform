package ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "weight_min", nullable = false)
    private Short weightMin;

    @Column(name = "weight_max", nullable = false)
    private Short weightMax;
}