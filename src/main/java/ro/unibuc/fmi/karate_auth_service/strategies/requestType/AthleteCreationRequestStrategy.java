package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.athlete.AthleteCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.services.AthleteService;
import ro.unibuc.fmi.karate_auth_service.services.ClubService;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AthleteCreationRequestStrategy extends AbstractRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.ATHLETE_CREATION;
    private final AthleteService athleteService;
    private final ClubService clubService;

    public AthleteCreationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory, AthleteService athleteService, ClubService clubService) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.athleteService = athleteService;
        this.clubService = clubService;
    }

    @Override
    protected boolean isUserAlreadyHasRequestedRole(User user) {
        return user.getRoles().contains(Role.ATHLETE);
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof AthleteCreationRequest existingCoachRequest && updatedRequest instanceof AthleteCreationRequest updatedCoachRequest) {
            existingCoachRequest.setAthleteRequest(updatedCoachRequest.getAthleteRequest());
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBePromoteToAthlete = request.getCreatedBy();
        AthleteRequest athleteRequest = mapperUtils.mapToAthleteRequest((AthleteCreationRequest) request);
        log.info("Creating athlete for user with email: {}", user.getEmail());
        athleteService.createAthlete(userToBePromoteToAthlete, athleteRequest);
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        Set<Coach> coaches = clubService.getCoachesForClub(((AthleteRequest) request).clubId());
        return coaches.stream()
                .map(Coach::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        AthleteCreationRequest athleteCreationRequest = mapperUtils.mapToAthleteCreationRequest((AthleteRequest) request);
        athleteCreationRequest.prePersist();
        return athleteCreationRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.mapToAthleteCreationResponse((AthleteCreationRequest) request);
    }
}
