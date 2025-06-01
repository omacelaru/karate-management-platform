package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.CompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.manager.AthleteCategoryRegistrationManager;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Set;

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


    private Competition findCompetitionById(Long competitionId) {
        return competitionRepository.findById(competitionId).orElseThrow(() -> {
            log.error("Competition with ID {} not found", competitionId);
            return new IllegalArgumentException("Competition not found");
        });
    }

    public List<Competition> getAllCompetitionsForSeeder() {
        log.info("Getting all competitions for seeder");

        return competitionRepository.findAll();
    }
}
