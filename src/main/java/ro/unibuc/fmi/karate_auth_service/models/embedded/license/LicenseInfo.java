package ro.unibuc.fmi.karate_auth_service.models.embedded.license;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

@Embeddable
public record LicenseInfo(
        @Column(name = "license_series", nullable = false)
        @Pattern(regexp = "^[a-z]{2}$", message = "Series must have 2 letters")
        String series,

        @Pattern(regexp = "^[0-9]{6}$", message = "Number must have 6 digits")
        @Column(name = "license_number", nullable = false)
        String number
) {
}