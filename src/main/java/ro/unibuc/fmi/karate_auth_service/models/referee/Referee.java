package ro.unibuc.fmi.karate_auth_service.models.referee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "referees")
public class Referee extends BaseEntity {
    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;

    @Embedded
    private LicenseInfo licenseInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private RefereeCategory category;
}
