package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.AthleteRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.CompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.manager.AthleteCategoryRegistrationManager;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.AthleteCompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.request.AthleteCompetitionRegistrationRequestRepository;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AthleteCompetitionRegistrationRequestStrategy extends AbstractRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.ATHLETE_COMPETITION_REGISTRATION;
    private final AthleteService athleteService;
    private final CompetitionService competitionService;
    private final AthleteCompetitionRegistrationRequestRepository athleteCompetitionRegistrationRequestRepository;
    private final AthleteCategoryRegistrationManager athleteCategoryRegistrationManager;
    private final MapperUtils mapperUtils;

    public AthleteCompetitionRegistrationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory,
                                                         AthleteService athleteService, CompetitionService competitionService, AthleteCompetitionRegistrationRequestRepository athleteCompetitionRegistrationRequestRepository, AthleteCategoryRegistrationManager athleteCategoryRegistrationManager) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.athleteService = athleteService;
        this.competitionService = competitionService;
        this.mapperUtils = mapperUtils;
        this.athleteCompetitionRegistrationRequestRepository = athleteCompetitionRegistrationRequestRepository;
        this.athleteCategoryRegistrationManager = athleteCategoryRegistrationManager;
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        log.info("Getting requests made by user with email: {}", user.getEmail());
        return athleteCompetitionRegistrationRequestRepository.findAllByCreatedById(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    protected void validateRequest(User user, Object request) {
        AthleteRegistrationRequest registrationRequest = (AthleteRegistrationRequest) request;

        // Validate competition exists
        Competition competition = competitionService.findCompetitionById(registrationRequest.competitionId());

        // Validate at least one category is selected
        if ((registrationRequest.individualCategories() == null || registrationRequest.individualCategories().isEmpty()) &&
                (registrationRequest.teamCategories() == null || registrationRequest.teamCategories().isEmpty())) {
            throw new IllegalArgumentException("At least one category must be selected");
        }

        // Validate competition is not already finished
        if (competition.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot register for a finished competition");
        }

        // Validate athlete has a team if team categories are selected
        if (registrationRequest.teamCategories() != null && !registrationRequest.teamCategories().isEmpty()) {
            if (athleteService.getTeamsByAthleteId(user.getId()).isEmpty()) {
                throw new IllegalArgumentException("Athlete must be part of a team to register for team categories");
            }
        }
    }

    @Override
    protected boolean isDuplicateRequest(User user, Object request) {
        return athleteCompetitionRegistrationRequestRepository.existsByCreatedBy_IdAndCompetitionId(user.getId(), ((AthleteRegistrationRequest) request).competitionId());
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof AthleteCompetitionRegistrationRequest existingRegistrationRequest &&
                updatedRequest instanceof AthleteCompetitionRegistrationRequest updatedRegistrationRequest) {
            existingRegistrationRequest.setCompetitionId(updatedRegistrationRequest.getCompetitionId());
            existingRegistrationRequest.setIndividualCategories(updatedRegistrationRequest.getIndividualCategories());
            existingRegistrationRequest.setTeamCategories(updatedRegistrationRequest.getTeamCategories());
        } else {
            throw new IllegalArgumentException("Invalid request type for updating fields");
        }
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBeRegistered = request.getCreatedBy();
        AthleteCompetitionRegistrationRequest registrationRequest = (AthleteCompetitionRegistrationRequest) request;
        CompetitionRegistrationRequest competitionRegistrationRequest = new CompetitionRegistrationRequest(
                Map.of(userToBeRegistered.getId(), registrationRequest.getIndividualCategories()),
                Map.of(athleteService.getTeamsByAthleteId(userToBeRegistered.getId()).stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No team found for athlete"))
                        .getId(), registrationRequest.getTeamCategories()));
        Competition competition = competitionService.findCompetitionById(registrationRequest.getCompetitionId());
        athleteCategoryRegistrationManager.registerAthletesToCompetition(competitionRegistrationRequest, competition);
        log.info("Athlete {} registered to competition {}", userToBeRegistered.getEmail(), competition.getName());
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return athleteService.findCoachesByAthleteId(user.getId())
                .stream()
                .map(Coach::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        AthleteCompetitionRegistrationRequest registrationRequest = mapperUtils.toAthleteCompetitionRegistrationRequest((AthleteRegistrationRequest) request);
        registrationRequest.prePersist();
        return registrationRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.maptoAthleteCompetitionRegistrationResponse((AthleteCompetitionRegistrationRequest) request);
    }
}