package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.IndividualCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.TeamCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes.CategoryWithAthletesResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes.IndividualCategoryWithAthletesResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes.TeamCategoryWithTeamsResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.CompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;
import ro.unibuc.fmi.karate_management_platform.manager.AthleteCategoryRegistrationManager;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final CategoryService categoryService;
    private final MapperUtils mapperUtils;
    private final OrganizerRepository organizerRepository;
    private final AthleteCategoryRegistrationManager athleteCategoryRegistrationManager;
    private final CoachService coachService;

    @Transactional
    public Competition createCompetition(User createdBy, CompetitionRequest competitionRequest) {
        log.info("Creating competition {}", competitionRequest.name());

        Organizer organizer = organizerRepository.findByUserEmail(createdBy.getEmail()).orElseThrow();

        Set<Category> categories = categoryService.getDefaultCategoryEntities();

        Competition competition = mapperUtils.mapToCompetition(competitionRequest);
        competition.setCategories(categories);
        competition.setOrganizer(organizer);

        return competitionRepository.save(competition);
    }

    public Page<CompetitionResponse> getAllCompetitions(Pageable pageable) {
        log.info("Getting all competitions paginated");

        return competitionRepository.findAll(pageable)
                .map(mapperUtils::mapToCompetitionResponse);
    }

    public CompetitionResponse getCompetitionById(Long competitionId) {
        log.info("Getting competition by ID {}", competitionId);

        Competition competition = findCompetitionById(competitionId);

        return mapperUtils.mapToCompetitionResponse(competition);
    }

    @Transactional
    public CompetitionResponse registerAthletesToCompetition(User user, Long competitionId, CompetitionRegistrationRequest competitionRegistrationRequest) {
        log.info("Registering athletes from coach {} to competition with ID {}", user.getEmail(), competitionId);

        Coach coach = coachService.findCoachByEmail(user.getEmail());
        Competition competition = findCompetitionById(competitionId);

        // Remove athletes that are not coached by the coach (checked also for team members)
        competitionRegistrationRequest.individualCategories().keySet()
                .forEach(athleteId -> {
                    if (coach.getAthletes().stream().noneMatch(athlete -> athlete.getId().equals(athleteId))) {
                        log.warn("Athlete with ID {} is not coached by the coach", athleteId);
                    }
                });
        Competition competitionUpdated = athleteCategoryRegistrationManager.registerAthletesToCompetition(competitionRegistrationRequest, competition);

        competitionRepository.save(competitionUpdated);

        return mapperUtils.mapToCompetitionResponse(competitionUpdated);
    }


    private Athlete findAthleteById(Long athleteId, Coach coach) {
        return coach.getAthletes().stream()
                .filter(a -> a.getId().equals(athleteId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Athlete not found"));
    }

//    private void registerAthleteToKumite(Athlete athlete, AgeGroup ageGroup, Set<KumiteCategory> kumiteCategories) {
//        Short weight = athlete.getWeight();
//        kumiteCategories.stream()
//                .filter(category -> category.getAgeGroup().equals(ageGroup))
//                .filter(category -> category.getGender().equals(athlete.getUser().getGender()))
//                .filter(category -> weight >= category.getWeightMin() && weight <= category.getWeightMax())
//                .findFirst()
//                .ifPresent(category -> category.getAthletes().add(athlete));
//    }
//
//    private void registerAthleteToKata(Athlete athlete, AgeGroup ageGroup, Set<KataCategory> kataCategories) {
//        KataBeltRange kataCategoryType = KataBeltRange.getKataCategoryTypeByBelt(athlete.getBelt());
//        kataCategories.stream()
//                .filter(category -> category.getAgeGroup().equals(ageGroup))
//                .filter(category -> category.getGender().equals(athlete.getUser().getGender()))
//                .filter(category -> category.getKataBeltRange().equals(kataCategoryType))
//                .findFirst()
//                .ifPresent(category -> category.getAthletes().add(athlete));
//    }


    public Competition findCompetitionById(Long competitionId) {
        return competitionRepository.findById(competitionId).orElseThrow(() -> {
            log.error("Competition with ID {} not found", competitionId);
            return new IllegalArgumentException("Competition not found");
        });
    }

    public List<Competition> getAllCompetitionsForSeeder() {
        log.info("Getting all competitions for seeder");

        return competitionRepository.findAll();
    }

    public Set<CategoryWithAthletesResponse> getCategoriesWithAthletes(Long competitionId) {
        Competition competition = findCompetitionById(competitionId);

        Set<CategoryWithAthletesResponse> individualCategories = competition.getCategories().stream()
                .filter(category -> category instanceof IndividualCategory)
                .filter(category -> !((IndividualCategory) category).getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .map(category -> {
                    List<AthleteResponse> athletes = ((IndividualCategory) category).getParticipations().stream()
                            .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                            .map(participation -> mapperUtils.mapToAthleteResponse(participation.getAthlete()))
                            .sorted(Comparator.comparing(AthleteResponse::getFullName))
                            .collect(Collectors.toList());

                    return IndividualCategoryWithAthletesResponse.builder()
                            .category(mapperUtils.mapCategory(category))
                            .athletes(athletes)
                            .build();
                })
                .collect(Collectors.toSet());

        Set<CategoryWithAthletesResponse> teamCategories = competition.getCategories().stream()
                .filter(category -> category instanceof TeamCategory)
                .filter(category -> !((TeamCategory) category).getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .map(category -> {
                    List<TeamResponse> teams = ((TeamCategory) category).getParticipations().stream()
                            .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                            .map(participation -> mapperUtils.mapToTeamResponse(participation.getTeam()))
                            .sorted(Comparator.comparing(TeamResponse::getTeamName))
                            .toList();


                    return TeamCategoryWithTeamsResponse.builder()
                            .category(mapperUtils.mapCategory(category))
                            .teams(teams)
                            .build();
                })
                .collect(Collectors.toSet());

        //sorted by age group, gender, and category type
        return Stream.concat(individualCategories.stream(), teamCategories.stream())
                .sorted(Comparator.comparing(CategoryWithAthletesResponse::getCategory, Comparator.comparing(CategoryResponse::getAgeGroup))
                        .thenComparing(CategoryWithAthletesResponse::getCategory, Comparator.comparing(CategoryResponse::getGender))
                        .thenComparing(category -> {
                            CategoryResponse cat = category.getCategory();
                            if (cat instanceof IndividualCategoryResponse) {
                                return ((IndividualCategoryResponse) cat).getCategoryType().name();
                            } else if (cat instanceof TeamCategoryResponse) {
                                return ((TeamCategoryResponse) cat).getCategoryType().name();
                            }
                            return "";
                        }))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public CompetitionResponse toggleRegistrationStatus(User user, Long competitionId, boolean open) {
        log.info("Toggling registration status for competition {} to {}", competitionId, open);

        Competition competition = findCompetitionById(competitionId);
        
        // Verify that the user is the organizer of this competition
        if (!competition.getOrganizer().getUser().getEmail().equals(user.getEmail())) {
            log.error("User {} is not authorized to modify competition {}", user.getEmail(), competitionId);
            throw new IllegalArgumentException("You are not authorized to modify this competition");
        }

        competition.setRegistrationOpen(open);
        competition = competitionRepository.save(competition);

        return mapperUtils.mapToCompetitionResponse(competition);
    }

    public Page<CompetitionResponse> getCompetitionsByOrganizer(User user, Pageable pageable) {
        log.info("Getting competitions for organizer {}", user.getEmail());

        Organizer organizer = organizerRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        return competitionRepository.findAllByOrganizer(organizer, pageable)
                .map(mapperUtils::mapToCompetitionResponse);
    }
}
