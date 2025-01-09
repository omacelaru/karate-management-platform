package ro.unibuc.fmi.karate_auth_service.models.athelte;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

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

    @Column(name = "height")
    @NotNull
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 250, message = "Height must be no greater than 250 cm")
    private Integer height;

    @Column(name = "weight")
    @NotNull
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 200, message = "Weight must be no greater than 200 kg")
    private Integer weight;

    @NotNull(message = "At least one coach is required")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "athlete_coach",
            joinColumns = @JoinColumn(name = "athlete_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id")
    )
    private Set<Coach> coaches = new LinkedHashSet<>();

}
