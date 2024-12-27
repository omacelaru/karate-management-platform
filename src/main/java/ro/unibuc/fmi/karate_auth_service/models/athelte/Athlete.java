package ro.unibuc.fmi.karate_auth_service.models.athelte;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_auth_service.models.BaseEntity;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "athletes")
public class Athlete extends BaseEntity {
    @Column(name = "nationality", length = 100)
    @NotNull(message = "Nationality cannot be null")
    @Size(min = 2, max = 100, message = "Nationality should be between 2 and 100 characters")
    private String nationality;

    @Column(name = "birth_date")
    @NotNull(message = "Birth date cannot be null")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Column(name = "height")
    @NotNull
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 250, message = "Height must be no greater than 250 cm")
    private Integer height;

    @NotNull
    @Column(name = "weight")
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 200, message = "Weight must be no greater than 200 kg")
    private Integer weight;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    private User user;
}
