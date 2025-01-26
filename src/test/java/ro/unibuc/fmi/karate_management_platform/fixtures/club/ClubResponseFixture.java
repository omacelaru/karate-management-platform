package ro.unibuc.fmi.karate_management_platform.fixtures.club;

import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;

public class ClubResponseFixture {

    public static ClubResponse createDefaultClubResponse() {
        return new ClubResponse(
                1L,
                "Karate Club",
                "KTC"
        );
    }

    public static ClubResponse createCustomClubResponse(Long id, String name, String acronym) {
        return new ClubResponse(
                id,
                name,
                acronym
        );
    }
}