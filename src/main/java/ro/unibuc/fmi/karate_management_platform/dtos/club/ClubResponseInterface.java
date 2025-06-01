package ro.unibuc.fmi.karate_management_platform.dtos.club;

public sealed interface ClubResponseInterface permits ClubResponse, ClubWithCoachesResponse {
    Long id();

    String name();

    String acronym();

    String city();

    String address();

    String phone();

    String email();
}
