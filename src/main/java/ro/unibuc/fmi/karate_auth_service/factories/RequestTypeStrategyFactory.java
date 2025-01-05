package ro.unibuc.fmi.karate_auth_service.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.strategies.requestType.RequestTypeStrategy;

import java.util.List;
import java.util.Map;

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
            throw new IllegalArgumentException("No strategy found for request type: " + requestType);
        }
        return strategy;
    }
}
