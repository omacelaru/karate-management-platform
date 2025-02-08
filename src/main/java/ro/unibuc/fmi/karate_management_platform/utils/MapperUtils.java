package ro.unibuc.fmi.karate_management_platform.utils;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.KataCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.KumiteCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.request.*;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.club.Club;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteCategory;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.referee.Referee;
import ro.unibuc.fmi.karate_management_platform.models.request.athlete.AthleteCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.CompetitionCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.organizer.OrganizerCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

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

    RefereeResponse mapToRefereeResponse(Referee referee);

    RefereeCreationRequest mapToRefereeCreationRequest(@Valid RefereeRequest refereeRequest);

    RefereeCreationResponse mapToRefereeCreationResponse(RefereeCreationRequest refereeCreationRequest);

    @Mapping(target = "licenseInfo", source = "refereeRequest.licenseInfo")
    @Mapping(target = "level", source = "refereeRequest.level")
    RefereeRequest mapToRefereeRequest(RefereeCreationRequest request);


    // === Organizer Mapping ===
    Organizer mapToOrganizer(@Valid OrganizerRequest organizerRequest);

    OrganizerResponse mapToOrganizerResponse(Organizer organizer);

    @Mapping(target = "taxRegistrationNumber", source = "organizerRequest.taxRegistrationNumber")
    OrganizerRequest mapToOrganizerRequest(OrganizerCreationRequest request);

    OrganizerCreationRequest mapToOrganizerCreationRequest(@Valid OrganizerRequest organizerRequest);

    OrganizerCreationResponse mapToOrganizerCreationResponse(OrganizerCreationRequest request);


    // === Competition Mapping ===
    CompetitionCreationResponse mapToCompetitionCreationResponse(CompetitionCreationRequest request);

    @Mapping(target = "categoriesIds", source = "competitionRequest.categoriesIds")
    @Mapping(target = "date", source = "competitionRequest.date")
    @Mapping(target = "location", source = "competitionRequest.location")
    @Mapping(target = "name", source = "competitionRequest.name")
    CompetitionRequest mapToCompetitionRequest(CompetitionCreationRequest request);

    CompetitionCreationRequest mapToCompetitionCreationRequest(@Valid CompetitionRequest competitionRequest);

    Competition mapToCompetition(CompetitionRequest competitionRequest);

    CompetitionResponse mapToCompetitionResponse(Competition competition);

    KataCategoryResponse mapToKataCategoryResponse(KataCategory kataCategory);

    KumiteCategoryResponse mapToKumiteCategoryResponse(KumiteCategory kumiteCategory);

    default CategoryResponse mapCategory(Category category) {
        if (category instanceof KataCategory) {
            return mapToKataCategoryResponse((KataCategory) category);
        } else if (category instanceof KumiteCategory) {
            return mapToKumiteCategoryResponse((KumiteCategory) category);
        } else {
            throw new IllegalArgumentException("Unknown category type: " + category.getClass());
        }
    }
}

