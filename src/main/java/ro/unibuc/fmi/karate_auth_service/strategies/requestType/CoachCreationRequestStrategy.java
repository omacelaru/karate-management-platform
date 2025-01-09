package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.services.CoachService;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Component
public class CoachCreationRequestStrategy extends AbstractRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.COACH_CREATION;
    private final CoachService coachService;

    @Autowired
    public CoachCreationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory, CoachService coachService) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.coachService = coachService;
    }

    @Override
    protected boolean isUserAlreadyHasRequestedRole(User user) {
        return user.getRoles().contains(Role.COACH);
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof CoachCreationRequest existingCoachRequest && updatedRequest instanceof CoachCreationRequest updatedCoachRequest) {
            existingCoachRequest.setCoachRequest(updatedCoachRequest.getCoachRequest());
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBePromoteToCoach = request.getCreatedBy();
        CoachRequest coachRequest = mapperUtils.mapToCoachRequest((CoachCreationRequest) request);
        log.info("Creating coach for user with email: {}", user.getEmail());
        coachService.createCoach(userToBePromoteToCoach, coachRequest);
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return Set.of(Role.ADMIN);
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        CoachCreationRequest creationRequest = mapperUtils.mapToCoachCreationRequest((CoachRequest) request);
        //TODO - auto call prePersist
        creationRequest.prePersist();
        return creationRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.mapToCoachCreationResponse((CoachCreationRequest) request);
    }
}
