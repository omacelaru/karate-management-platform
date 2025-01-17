package ro.unibuc.fmi.karate_auth_service.models.embedded.TRN;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

@Embeddable
public record TaxRegistrationNumber(
        @Column(name = "trn_series", nullable = false)
        @Pattern(regexp = "^[0-9]{8}$", message = "Series must have 8 numbers")
        String number
) {
}
