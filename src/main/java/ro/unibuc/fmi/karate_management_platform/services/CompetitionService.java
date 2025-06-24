package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.IndividualCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.TeamCategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.participation.CategoryParticipationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.participation.IndividualCategoryParticipationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.participation.TeamCategoryParticipationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.CompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.scheduling.ScheduledCategory;
import ro.unibuc.fmi.karate_management_platform.manager.AthleteCategoryRegistrationManager;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryScheduling;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalTime;
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
    private final CompetitionSchedulingService competitionSchedulingService;

    @Transactional
    public Competition createCompetition(User createdBy, CompetitionRequest competitionRequest) {
        log.info("Creating competition {}", competitionRequest.name());

        Organizer organizer = organizerRepository.findByUserEmail(createdBy.getEmail()).orElseThrow();

        Set<Category> categories = categoryService.getDefaultCategoryEntities();

        Competition competition = mapperUtils.mapToCompetition(competitionRequest);
        competition.setCategories(categories);
        competition.setOrganizer(organizer);
        competition.setRegistrationOpen(true);

        return competitionRepository.save(competition);
    }

    public Page<CompetitionResponse> getAllCompetitions(Pageable pageable) {
        log.info("Getting all competitions paginated");

        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "date"));
        return competitionRepository.findAll(pageableSorted)
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

    public Set<Category> getCategoriesWithAthletesEntity(Long competitionId) {
        Competition competition = findCompetitionById(competitionId);
        log.info("Getting categories with athletes for competition {}", competitionId);

        Set<Category> categoriesWithAthletes = competition.getCategories().stream()
                .filter(category -> category instanceof IndividualCategory || category instanceof TeamCategory)
                .filter(category -> !category.getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .collect(Collectors.toSet());
        if (categoriesWithAthletes.isEmpty()) {
            log.warn("No categories with athletes found for competition {}", competitionId);
        } else {
            log.info("Found {} categories with athletes for competition {}", categoriesWithAthletes.size(), competitionId);
        }
        return categoriesWithAthletes;
    }

    public Set<CategoryResponse> getCategoriesWithAthletes(Long competitionId) {
        Competition competition = findCompetitionById(competitionId);

        Set<Category> categoriesWithAthletes = getCategoriesWithAthletesEntity(competitionId);

        Set<CategoryResponse> individualCategories = competition.getCategories().stream()
                .filter(category -> category instanceof IndividualCategory)
                .filter(category -> !category.getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .map(category -> {
                    Set<CategoryParticipationResponse> participations = ((IndividualCategory) category).getParticipations().stream()
                            .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                            .map(participation -> {
                                IndividualCategoryParticipationResponse response = new IndividualCategoryParticipationResponse();
                                response.setId(participation.getId());
                                response.setCompetitionId(participation.getCompetition().getId());
                                response.setDurationMinutes(participation.getDurationMinutes());
                                response.setAthlete(mapperUtils.mapToAthleteResponse(((IndividualCategoryParticipation)participation).getAthlete()));
                                return response;
                            })
                            .collect(Collectors.toSet());

                    CategoryResponse response = mapperUtils.mapCategory(category);
                    response.setParticipations(participations);
                    return response;
                })
                .collect(Collectors.toSet());

        Set<CategoryResponse> teamCategories = competition.getCategories().stream()
                .filter(category -> category instanceof TeamCategory)
                .filter(category -> !category.getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .map(category -> {
                    Set<CategoryParticipationResponse> participations = ((TeamCategory) category).getParticipations().stream()
                            .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                            .map(participation -> {
                                TeamCategoryParticipationResponse response = new TeamCategoryParticipationResponse();
                                response.setId(participation.getId());
                                response.setCompetitionId(participation.getCompetition().getId());
                                response.setDurationMinutes(participation.getDurationMinutes());
                                response.setTeam(mapperUtils.mapToTeamResponse(((TeamCategoryParticipation)participation).getTeam()));
                                return response;
                            })
                            .collect(Collectors.toSet());

                    CategoryResponse response = mapperUtils.mapCategory(category);
                    response.setParticipations(participations);
                    return response;
                })
                .collect(Collectors.toSet());

        //sorted by age group, gender, and category type
        return Stream.concat(individualCategories.stream(), teamCategories.stream())
                .sorted(Comparator.comparing(CategoryResponse::getAgeGroup)
                        .thenComparing(CategoryResponse::getGender)
                        .thenComparing(category -> {
                            if (category instanceof IndividualCategoryResponse) {
                                return ((IndividualCategoryResponse) category).getCategoryType().name();
                            } else if (category instanceof TeamCategoryResponse) {
                                return ((TeamCategoryResponse) category).getCategoryType().name();
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

        if (!open) {
            List<ScheduledCategory> greedySchedule = competitionSchedulingService.scheduleCategories(competitionId);
            List<ScheduledCategory> graphColoringSchedule = competitionSchedulingService.scheduleCategoriesGraphColoring(competitionId);


            LocalTime greedyEnd = greedySchedule.stream().map(ScheduledCategory::getEndTime).max(LocalTime::compareTo).orElse(LocalTime.MIN);
            LocalTime graphEnd = graphColoringSchedule.stream().map(ScheduledCategory::getEndTime).max(LocalTime::compareTo).orElse(LocalTime.MIN);

            log.info("Greedy schedule end time: {}", greedyEnd);
            log.info("Graph coloring schedule end time: {}", graphEnd);

            List<ScheduledCategory> chosenSchedule;
            String chosenAlgorithm;
//            if (greedyEnd.isBefore(graphEnd)) {
//                chosenSchedule = greedySchedule;
//                chosenAlgorithm = "greedy";
//            } else {
                chosenSchedule = graphColoringSchedule;
                chosenAlgorithm = "graph_coloring";
//            }

            log.info("Chosen schedule algorithm: {} (end time: {})", chosenAlgorithm, (chosenAlgorithm.equals("greedy") ? greedyEnd : graphEnd));
            chosenSchedule.forEach(scheduled ->
                log.info("Category {} scheduled on tatami {} from {} to {} (duration: {} minutes)",
                    scheduled.getCategoryName(),
                    scheduled.getTatamiId(),
                    scheduled.getStartTime(),
                    scheduled.getEndTime(),
                    scheduled.getTotalDuration()
                )
            );
        }

        return mapperUtils.mapToCompetitionResponse(competition);
    }

    public List<CompetitionResponse> getCompetitionsByOrganizer(User user, Pageable pageable) {
        log.info("Getting competitions for organizer {}", user.getEmail());

        Organizer organizer = organizerRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        return competitionRepository.findAllByOrganizer(organizer, pageable)
                .stream()
                .map(mapperUtils::mapToCompetitionResponse)
                .sorted(Comparator.comparing(CompetitionResponse::date))
                .toList();
    }

    public List<ScheduledCategory> getSavedScheduleForCompetition(Long competitionId) {
        List<CategoryScheduling> schedulings = competitionSchedulingService.findAllByCompetitionId(competitionId);
        return schedulings.stream().map(mapperUtils::mapToScheduledCategory).toList();
    }

    public ScheduledCategory getSavedScheduleForCategory(Long competitionId, Long categoryId) {
        CategoryScheduling scheduling = competitionSchedulingService.findByCompetitionIdAndCategoryId(competitionId, categoryId);
        return mapperUtils.mapToScheduledCategory(scheduling);
    }
}
