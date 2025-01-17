package ro.unibuc.fmi.karate_auth_service.models.coach;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.club.Club;
import ro.unibuc.fmi.karate_auth_service.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "coaches")
public class Coach extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;

    @Embedded
    private LicenseInfo licenseInfo;

    @ManyToMany(mappedBy = "coaches")
    private Set<Athlete> athletes = new LinkedHashSet<>();

    //TODO- set LAZY on all fetch types
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "club_id")
    private Club club;

}
