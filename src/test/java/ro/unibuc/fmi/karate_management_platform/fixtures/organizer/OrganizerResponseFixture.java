package ro.unibuc.fmi.karate_management_platform.fixtures.organizer;

import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;

public class OrganizerResponseFixture {
    public static OrganizerResponse createDefaultOrganizerResponse() {
        TaxRegistrationNumber trn = new TaxRegistrationNumber("1234567890");
        return new OrganizerResponse(
                UserResponseFixture.createDefaultUserResponse(),
                trn
        );
    }
}
