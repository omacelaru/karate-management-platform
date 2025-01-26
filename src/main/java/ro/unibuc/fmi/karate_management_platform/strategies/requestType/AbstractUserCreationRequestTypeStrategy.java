package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

@Slf4j
@Component
public abstract class AbstractUserCreationRequestTypeStrategy extends AbstractRequestTypeStrategy {

    public AbstractUserCreationRequestTypeStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
    }

    @Override
    protected void validateRequest(User user, Object request) {
        if (isUserAlreadyHasRequestedRole(user)) {
            log.error("User {} already has the requested role", user.getId());
            throw new IllegalStateException("User already has the requested role.");
        }
    }

    protected abstract boolean isUserAlreadyHasRequestedRole(User user);

}
