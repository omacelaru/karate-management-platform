package ro.unibuc.fmi.karate_management_platform.models.club;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;

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
    //todo - scriere despre cascade type
    @OneToMany(mappedBy = "club", cascade = CascadeType.DETACH)
    private Set<Coach> coaches = new LinkedHashSet<>();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "acronym", nullable = false, unique = true)
    private String acronym;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;
}
