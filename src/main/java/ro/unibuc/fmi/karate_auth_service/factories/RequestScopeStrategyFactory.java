package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;
import ro.unibuc.fmi.karate_auth_service.strategies.requestScope.RequestScopeStrategy;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RequestScopeStrategyFactory {
    private final Map<RequestScope, RequestScopeStrategy> strategies;

    @Autowired
    public RequestScopeStrategyFactory(List<RequestScopeStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                RequestScopeStrategy::getRequestScope,
                                strategy -> strategy
                        )
                );
    }

    public RequestScopeStrategy getStrategy(RequestScope requestScope) {
        RequestScopeStrategy strategy = strategies.get(requestScope);
        if (strategy == null) {
            log.error("No strategy found for request scope: {}", requestScope);
            throw new IllegalArgumentException("No strategy found for request scope: " + requestScope);
        }
        return strategy;
    }
}
