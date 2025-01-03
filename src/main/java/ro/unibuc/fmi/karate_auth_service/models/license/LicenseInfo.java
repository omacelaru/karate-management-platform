package ro.unibuc.fmi.karate_auth_service.models.license;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record LicenseInfo(
        @Column(name = "license_series", nullable = false)
        String series,

        @Column(name = "license_number", nullable = false)
        String number
) {
}