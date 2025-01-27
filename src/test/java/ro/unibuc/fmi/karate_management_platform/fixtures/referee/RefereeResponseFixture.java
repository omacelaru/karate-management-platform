package ro.unibuc.fmi.karate_management_platform.fixtures.referee;

import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_management_platform.models.referee.RefereeLevel;

public class RefereeResponseFixture {
    public static RefereeResponse createDefaultRefereeResponse() {
        LicenseInfo licenseInfo = new LicenseInfo("aa", "111111");
        return new RefereeResponse(
                UserResponseFixture.createDefaultUserResponse(),
                licenseInfo,
                RefereeLevel.A
        );
    }
}
