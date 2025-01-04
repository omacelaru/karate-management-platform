package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.repositories.RequestInfoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInfoRepositoryFactory {
    private final CoachCreationRequestRepository coachCreationRequestRepository;

    public RequestInfoRepository<? extends RequestInfo> getRepository(Class<? extends RequestInfoRepository<? extends RequestInfo>> repositoryClass) {
        if (repositoryClass.equals(CoachCreationRequestRepository.class)) {
            return coachCreationRequestRepository;
        }
        log.error("No repository found for class: {}", repositoryClass);
        throw new IllegalArgumentException("No repository found for class: " + repositoryClass);
    }
}
