package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Component
public class CompetitionCreationRequestStrategy extends AbstractRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.COMPETITION_CREATION;
    private final CompetitionService competitionService;

    public CompetitionCreationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory, CompetitionService competitionService) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.competitionService = competitionService;
    }

    @Override
    protected void validateRequest(User user, Object request) {

    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {

    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {

    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return Set.of();
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        return null;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return null;
    }
}
