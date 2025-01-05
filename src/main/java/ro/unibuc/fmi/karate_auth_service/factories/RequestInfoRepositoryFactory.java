package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.repositories.RequestInfoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInfoRepositoryFactory {
    private final CoachCreationRequestRepository coachCreationRequestRepository;

    public RequestInfoRepository<? extends RequestInfo> getRepository(RequestType requestType) {
        return switch (requestType) {
            case COACH_CREATION -> coachCreationRequestRepository;
            default -> {
                log.error("No repository found for request type: {}", requestType);
                throw new IllegalArgumentException("No repository found for request type: " + requestType);
            }
        };
    }
}
