package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.exceptions.RequestNotFoundException;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.strategies.requestType.RequestTypeStrategy;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RequestTypeStrategyFactory {
    private final Map<RequestType, RequestTypeStrategy> requestTypeStrategyMap;

    @Autowired
    public RequestTypeStrategyFactory(List<RequestTypeStrategy> strategies) {
        requestTypeStrategyMap = strategies.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                RequestTypeStrategy::getRequestType,
                                strategy -> strategy
                        )
                );
    }

    public RequestTypeStrategy getStrategy(RequestType requestType) {
        RequestTypeStrategy strategy = requestTypeStrategyMap.get(requestType);
        if (strategy == null) {
            log.error("No strategy found for request type: {}", requestType);
            throw new IllegalArgumentException("No strategy found for request type: " + requestType);
        }
        return strategy;
    }

    public RequestType getRequestType(Long requestId) {
        return requestTypeStrategyMap.values().stream()
                .filter(strategy -> strategy.isRequestType(requestId))
                .findFirst()
                .map(RequestTypeStrategy::getRequestType)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }
}
