package ro.unibuc.fmi.karate_management_platform.models.competition.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discipline_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "categories")
public abstract class Category extends BaseEntity {

    @Column(name = "age_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, targetEntity = Match.class, orphanRemoval = true)
    private List<Match> matches;
}
