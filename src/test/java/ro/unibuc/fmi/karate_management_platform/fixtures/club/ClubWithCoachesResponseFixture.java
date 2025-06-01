package ro.unibuc.fmi.karate_management_platform.fixtures.club;

import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;

import java.util.Set;

import static ro.unibuc.fmi.karate_management_platform.fixtures.coach.CoachResponseFixture.createDefaultCoachResponse;

public class ClubWithCoachesResponseFixture {

    public static ClubWithCoachesResponse createDefaultClubWithCoachesResponse() {
        return new ClubWithCoachesResponse(
                1L,
                Set.of(createDefaultCoachResponse()),
                "Karate Club",
                "KTC",
                "Bucharest",
                "Strada Exemplu 123",
                "+40712345678",
                "contact@karateclub.ro"
        );
    }

    public static ClubWithCoachesResponse createCustomClubWithCoachesResponse(
            Long id, String name, String acronym, Set<CoachResponse> coaches, String city, String address, String phone, String email) {
        return new ClubWithCoachesResponse(
                id,
                coaches,
                name,
                acronym,
                city,
                address,
                phone,
                email
        );
    }

}