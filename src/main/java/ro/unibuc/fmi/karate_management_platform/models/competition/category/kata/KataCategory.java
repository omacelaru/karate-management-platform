package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata;

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
public class KataCategory extends Category {
    @Enumerated(EnumType.STRING)
    @Column(name = "kata_belt_range", nullable = false)
    private KataBeltRange kataBeltRange;
}