package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.services.RefereeService;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

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
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBePromoteToReferee = request.getCreatedBy();
        RefereeRequest refereeRequest = mapperUtils.mapToRefereeRequest((RefereeCreationRequest) request);
        log.info("Creating referee for user with email: {}", user.getEmail());
        refereeService.createReferee(userToBePromoteToReferee, refereeRequest);
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
