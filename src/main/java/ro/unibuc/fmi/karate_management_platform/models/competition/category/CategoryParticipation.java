package ro.unibuc.fmi.karate_management_platform.models.competition.category;

import jakarta.persistence.*;
import lombok.*;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;

@Entity
@Table(name = "category_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CategoryParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Competition competition;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
} 