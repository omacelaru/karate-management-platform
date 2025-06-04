package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

import jakarta.persistence.*;
import lombok.*;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;

@Entity
@Table(name = "individual_category_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualCategoryParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private IndividualCategory category;

    @ManyToOne
    private Competition competition;

    @ManyToOne
    private Athlete athlete;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
}

