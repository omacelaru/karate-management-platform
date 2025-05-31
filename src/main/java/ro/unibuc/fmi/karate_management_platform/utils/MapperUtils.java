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
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.*;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;
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
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.*;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.referee.Referee;
import ro.unibuc.fmi.karate_management_platform.models.request.athlete.AthleteCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.CompetitionCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.organizer.OrganizerCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.Objects;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MapperUtils {

    // === Auth Mapping ===
    AuthResponse mapToAuthResponse(String accessToken, String refreshToken);


    // === User Mapping ===
    UserResponse mapToUserResponse(User user);


    // === Athlete Mapping ===
    @Mapping(target = "clubResponse", expression = "java(getFirstClubResponse(athlete))")
    AthleteResponse mapToAthleteResponse(Athlete athlete);

    default Optional<ClubResponse> getFirstClubResponse(Athlete athlete) {
        return athlete.getCoaches().stream()
                .map(Coach::getClub)
                .filter(Objects::nonNull)
                .findFirst()
                .map(this::mapToClubResponse);
    }


    Athlete mapToAthlete(AthleteRequest athleteRequest);

    @Mapping(target = "clubId", source = "athleteRequest.clubId")
    @Mapping(target = "height", source = "athleteRequest.height")
    @Mapping(target = "weight", source = "athleteRequest.weight")
    @Mapping(target = "belt", source = "athleteRequest.belt")
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

    @Mapping(target = "date", source = "competitionRequest.date")
    @Mapping(target = "location", source = "competitionRequest.location")
    @Mapping(target = "name", source = "competitionRequest.name")
    CompetitionRequest mapToCompetitionRequest(CompetitionCreationRequest request);

    CompetitionCreationRequest mapToCompetitionCreationRequest(@Valid CompetitionRequest competitionRequest);

    Competition mapToCompetition(CompetitionRequest competitionRequest);

    CompetitionResponse mapToCompetitionResponse(Competition competition);

    KataCategoryResponse mapToKataIndividualCategoryResponse(KataIndividualCategory kataIndividualCategory);

    KumiteCategoryResponse mapToKumiteIndividualCategoryResponse(KumiteIndividualCategory kumiteIndividualCategory);

    KataCategoryResponse mapToKataTeamCategoryResponse(KataTeamCategory kataTeamCategory);

    KumiteCategoryResponse mapToKumiteTeamCategoryResponse(KumiteTeamCategory kumiteTeamCategory);

    default CategoryResponse mapCategory(Category category) {
        return switch (category) {
            case KataIndividualCategory kataIndividualCategory ->
                    mapToKataIndividualCategoryResponse(kataIndividualCategory);
            case KumiteIndividualCategory kumiteIndividualCategory ->
                    mapToKumiteIndividualCategoryResponse(kumiteIndividualCategory);
            case KataTeamCategory kataTeamCategory -> mapToKataTeamCategoryResponse(kataTeamCategory);
            case KumiteTeamCategory kumiteTeamCategory -> mapToKumiteTeamCategoryResponse(kumiteTeamCategory);
            default -> throw new IllegalArgumentException("Unknown category type: " + category.getClass());
        };
    }

    IndividualKataMatchResponse mapToIndividualKataMatchResponse(IndividualKataMatch individualKataMatch);

    IndividualKumiteMatchResponse mapToIndividualKumiteMatchResponse(IndividualKumiteMatch individualKumiteMatch);

    TeamKataMatchResponse mapToTeamKataMatchResponse(TeamKataMatch teamKataMatch);

    TeamKumiteMatchResponse mapToTeamKumiteMatchResponse(TeamKumiteMatch teamKumiteMatch);

    default MatchResponse mapMatch(Match match) {
        return switch (match) {
            case IndividualKataMatch individualKataMatch -> mapToIndividualKataMatchResponse(individualKataMatch);
            case IndividualKumiteMatch individualKumiteMatch ->
                    mapToIndividualKumiteMatchResponse(individualKumiteMatch);
            case TeamKataMatch teamKataMatch -> mapToTeamKataMatchResponse(teamKataMatch);
            case TeamKumiteMatch teamKumiteMatch -> mapToTeamKumiteMatchResponse(teamKumiteMatch);
            case null, default -> {
                assert match != null;
                throw new IllegalArgumentException("Unknown match type: " + match.getClass());
            }
        };
    }

    TeamResponse mapToTeamResponse(Team savedTeam);
}

