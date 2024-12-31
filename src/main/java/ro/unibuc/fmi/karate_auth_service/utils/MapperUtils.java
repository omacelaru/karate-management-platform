package ro.unibuc.fmi.karate_auth_service.utils;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.club.Club;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

@Mapper(componentModel = "spring")
public interface MapperUtils {
    AuthResponse mapToAuthResponse(String accessToken, String refreshToken);

    UserResponse mapToUserResponse(User user);

    AthleteResponse mapToAthleteResponse(Athlete athlete);

    Athlete mapToAthlete(AthleteRequest athleteRequest);

    CoachResponse mapToCoachResponse(Coach coach);

    Coach mapToCoach(@Valid CoachRequest coachRequest);

    ClubWithCoachesResponse mapToClubWithCoachesResponse(Club club);

    ClubResponse mapToClubResponse(Club club);

    Club mapToClub(@Valid ClubRequest clubRequest);
}
