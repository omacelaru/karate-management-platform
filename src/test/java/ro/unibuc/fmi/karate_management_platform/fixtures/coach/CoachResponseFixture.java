package ro.unibuc.fmi.karate_management_platform.fixtures.coach;

import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.club.ClubResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;

import java.util.Set;

public class CoachResponseFixture {
    public static CoachResponse createDefaultCoachResponse() {
        LicenseInfo licenseInfo = new LicenseInfo(
                "aa",
                "111111"
        );
        ClubResponse club = ClubResponseFixture.createDefaultClubResponse();
        UserResponse user = UserResponseFixture.createDefaultUserResponse();
        return new CoachResponse(
                user,
                licenseInfo,
                club,
                Set.of(),
                Set.of()
        );
    }

    public static CoachResponse createCustomCoachResponse(UserResponse userResponse, LicenseInfo licenseInfo, ClubResponse club) {
        return new CoachResponse(
                userResponse,
                licenseInfo,
                club,
                Set.of(),
                Set.of()
        );
    }
}
