package ro.unibuc.fmi.karate_management_platform.models.organizer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "organizers")
public class Organizer extends BaseEntity {
    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;

    @Embedded
    private TaxRegistrationNumber taxRegistrationNumber;
}
