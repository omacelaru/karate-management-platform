package ro.unibuc.fmi.karate_auth_service.models.club;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "clubs")
public class Club extends BaseEntity {
    @OneToMany(mappedBy = "club", cascade = CascadeType.DETACH, orphanRemoval = true)
    private Set<Coach> coaches = new LinkedHashSet<>();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "acronym", nullable = false, unique = true)
    private String acronym;
}
