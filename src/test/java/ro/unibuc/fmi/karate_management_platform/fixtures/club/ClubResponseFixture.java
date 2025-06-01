package ro.unibuc.fmi.karate_management_platform.fixtures.club;

import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;

public class ClubResponseFixture {

    public static ClubResponse createDefaultClubResponse() {
        return new ClubResponse(
                1L,
                "Karate Club",
                "KTC",
                "Bucharest",
                "Strada Exemplu 123",
                "+40712345678",
                "contact@karateclub.ro"
        );
    }

    public static ClubResponse createCustomClubResponse(Long id, String name, String acronym, String city, String address, String phone, String email) {
        return new ClubResponse(
                id,
                name,
                acronym,
                city,
                address,
                phone,
                email
        );
    }
}