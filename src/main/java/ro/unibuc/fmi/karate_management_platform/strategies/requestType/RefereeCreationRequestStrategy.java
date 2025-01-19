package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.RefereeService;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Component
public class RefereeCreationRequestStrategy extends AbstractRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.REFEREE_CREATION;
    private final RefereeService refereeService;

    @Autowired
    public RefereeCreationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory, RefereeService refereeService) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.refereeService = refereeService;
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof RefereeCreationRequest existingRefereeRequest && updatedRequest instanceof RefereeCreationRequest updatedRefereeRequest) {
            existingRefereeRequest.setRefereeRequest(updatedRefereeRequest.getRefereeRequest());
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    @Override
    protected boolean isUserAlreadyHasRequestedRole(User user) {
        return user.getRoles().contains(Role.REFEREE);
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBePromoteToReferee = request.getCreatedBy();
        RefereeRequest refereeRequest = mapperUtils.mapToRefereeRequest((RefereeCreationRequest) request);
        log.info("Creating referee for user with email: {}", user.getEmail());
        refereeService.createReferee(userToBePromoteToReferee, refereeRequest);
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return Set.of(Role.ADMIN);
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        RefereeCreationRequest creationRequest = mapperUtils.mapToRefereeCreationRequest((RefereeRequest) request);
        //TODO - auto call prePersist
        creationRequest.prePersist();
        return creationRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.mapToRefereeCreationResponse((RefereeCreationRequest) request);
    }
}
