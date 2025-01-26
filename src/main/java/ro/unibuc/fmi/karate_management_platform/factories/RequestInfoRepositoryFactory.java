package ro.unibuc.fmi.karate_management_platform.factories;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.CompetitionCreationRequestRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.request.*;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInfoRepositoryFactory {
    private final CoachCreationRequestRepository coachCreationRequestRepository;
    private final RefereeCreationRequestRepository refereeCreationRequestRepository;
    private final AthleteCreationRequestRepository athleteCreationRequestRepository;
    private final OrganizerCreationRequestRepository organizerCreationRequestRepository;
    private final CompetitionCreationRequestRepository competitionCreationRequestRepository;

    private Map<RequestType, RequestInfoRepository<? extends RequestInfo>> repositoryMap;

    @PostConstruct
    private void initRepositoryMap() {
        repositoryMap = Map.of(
                RequestType.COACH_CREATION, coachCreationRequestRepository,
                RequestType.REFEREE_CREATION, refereeCreationRequestRepository,
                RequestType.ATHLETE_CREATION, athleteCreationRequestRepository,
                RequestType.ORGANIZER_CREATION, organizerCreationRequestRepository,
                RequestType.COMPETITION_CREATION, competitionCreationRequestRepository
        );
    }

    @SuppressWarnings("unchecked")
    public <T extends RequestInfo> RequestInfoRepository<T> getRepository(RequestType requestType) {
        RequestInfoRepository<? extends RequestInfo> repository = repositoryMap.get(requestType);
        if (repository == null) {
            log.error("No repository found for request type: {}", requestType);
            throw new IllegalArgumentException("No repository found for request type: " + requestType);
        }
        return (RequestInfoRepository<T>) repository;
    }
}
