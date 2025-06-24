package ro.unibuc.fmi.karate_management_platform.models.athelte;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "athletes")
public class Athlete extends BaseEntity {
    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "belt", nullable = false, length = 30)
    private Belt belt;

    @Column(name = "height")
    @NotNull
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 250, message = "Height must be no greater than 250 cm")
    private Short height;

    @Column(name = "weight")
    @NotNull
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 200, message = "Weight must be no greater than 200 kg")
    private Short weight;

    @NotNull(message = "At least one coach is required")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "athlete_coach",
            joinColumns = @JoinColumn(name = "athlete_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id")
    )
    private Set<Coach> coaches = new LinkedHashSet<>();

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "gold_medals", nullable = false)
    private int goldMedals = 0;

    @Column(name = "silver_medals", nullable = false)
    private int silverMedals = 0;

    @Column(name = "bronze_medals", nullable = false)
    private int bronzeMedals = 0;

}
