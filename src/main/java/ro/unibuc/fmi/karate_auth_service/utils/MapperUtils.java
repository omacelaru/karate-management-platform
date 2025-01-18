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
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.AthleteCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.request.OrganizerCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RefereeCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.club.Club;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.organizer.Organizer;
import ro.unibuc.fmi.karate_auth_service.models.referee.Referee;
import ro.unibuc.fmi.karate_auth_service.models.request.athlete.AthleteCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.organizer.OrganizerCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

@Mapper(componentModel = "spring")
public interface MapperUtils {

    // === Auth Mapping ===
    AuthResponse mapToAuthResponse(String accessToken, String refreshToken);


    // === User Mapping ===
    UserResponse mapToUserResponse(User user);


    // === Athlete Mapping ===
    AthleteResponse mapToAthleteResponse(Athlete athlete);

    Athlete mapToAthlete(AthleteRequest athleteRequest);

    @Mapping(target = "clubId", source = "athleteRequest.clubId")
    @Mapping(target = "height", source = "athleteRequest.height")
    @Mapping(target = "weight", source = "athleteRequest.weight")
    AthleteRequest mapToAthleteRequest(AthleteCreationRequest request);

    AthleteCreationResponse mapToAthleteCreationResponse(AthleteCreationRequest request);

    AthleteCreationRequest mapToAthleteCreationRequest(@Valid AthleteRequest athleteRequest);


    // === Coach Mapping ===
    CoachResponse mapToCoachResponse(Coach coach);

    Coach mapToCoach(@Valid CoachRequest coachRequest);

    @Mapping(target = "licenseInfo", source = "coachRequest.licenseInfo")
    CoachRequest mapToCoachRequest(CoachCreationRequest updatedRequest);

    CoachCreationRequest mapToCoachCreationRequest(@Valid CoachRequest coachRequest);

    CoachCreationResponse mapToCoachCreationResponse(CoachCreationRequest coachCreationRequest);


    // === Club Mapping ===
    ClubWithCoachesResponse mapToClubWithCoachesResponse(Club club);

    ClubResponse mapToClubResponse(Club club);

    Club mapToClub(@Valid ClubRequest clubRequest);


    // === Referee Mapping ===
    Referee mapToReferee(@Valid RefereeRequest refereeRequest);

    RefereeCreationRequest mapToRefereeCreationRequest(@Valid RefereeRequest refereeRequest);

    RefereeCreationResponse mapToRefereeCreationResponse(RefereeCreationRequest refereeCreationRequest);

    @Mapping(target = "licenseInfo", source = "refereeRequest.licenseInfo")
    @Mapping(target = "category", source = "refereeRequest.category")
    RefereeRequest mapToRefereeRequest(RefereeCreationRequest request);


    // === Organizer Mapping ===
    Organizer mapToOrganizer(@Valid OrganizerRequest organizerRequest);

    @Mapping(target = "taxRegistrationNumber", source = "organizerRequest.taxRegistrationNumber")
    OrganizerRequest mapToOrganizerRequest(OrganizerCreationRequest request);

    OrganizerCreationRequest mapToOrganizerCreationRequest(OrganizerRequest request);

    OrganizerCreationResponse mapToOrganizerCreationResponse(OrganizerCreationRequest request);
}

