package ro.unibuc.fmi.karate_auth_service.utils;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RefereeCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.club.Club;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.referee.Referee;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.referee.RefereeCreationRequest;
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

    CoachCreationRequest mapToCoachCreationRequest(@Valid CoachRequest coachRequest);

    @Mapping(target = "licenseInfo", source = "coachRequest.licenseInfo")
    CoachCreationResponse mapToCoachCreationResponse(CoachCreationRequest coachCreationRequest);

    @Mapping(target = "licenseInfo", source = "coachRequest.licenseInfo")
    CoachRequest mapToCoachRequest(CoachCreationRequest updatedRequest);

    RefereeCreationRequest mapToRefereeCreationRequest(@Valid RefereeRequest refereeRequest);

    @Mapping(target = "licenseInfo", source = "refereeRequest.licenseInfo")
    @Mapping(target = "category", source = "refereeRequest.category")
    RefereeCreationResponse mapToRefereeCreationResponse(RefereeCreationRequest refereeCreationRequest);

    @Mapping(target = "licenseInfo", source = "refereeRequest.licenseInfo")
    @Mapping(target = "category", source = "refereeRequest.category")
    RefereeRequest mapToRefereeRequest(RefereeCreationRequest request);

    Referee mapToReferee(@Valid RefereeRequest refereeRequest);
}
