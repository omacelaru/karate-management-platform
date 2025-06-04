package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryParticipation;

@Entity
@Table(name = "individual_category_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class IndividualCategoryParticipation extends CategoryParticipation {

    @ManyToOne
    private IndividualCategory category;

    @ManyToOne
    private Athlete athlete;
}

