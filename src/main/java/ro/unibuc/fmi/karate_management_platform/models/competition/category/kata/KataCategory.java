package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata;

import jakarta.persistence.*;
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
@DiscriminatorValue("KATA")
public class KataCategory extends Category {
    @Enumerated(EnumType.STRING)
    @Column(name = "kata_category_type", nullable = false)
    private KataCategoryType kataCategoryType;
}