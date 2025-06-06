package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.CompetitionCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
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
        if (!(request instanceof CompetitionRequest competitionRequest)) {
            throw new IllegalArgumentException("Invalid request type");
        }
        boolean duplicateExists = repositoryFactory.getRepository(requestType)
            .findAll()
            .stream()
            .filter(req -> req instanceof CompetitionCreationRequest)
            .map(req -> (CompetitionCreationRequest) req)
            .anyMatch(existingRequest -> 
                existingRequest.getCompetitionRequest().name().equals(competitionRequest.name()) &&
                existingRequest.getCompetitionRequest().location().equals(competitionRequest.location()) &&
                existingRequest.getCompetitionRequest().date().equals(competitionRequest.date()) &&
                existingRequest.getStatus().equals(RequestStatus.PENDING)
            );

        if (duplicateExists) {
            throw new IllegalArgumentException("Duplicate request found. You can edit the existing request");
        }
    }

    @Override
    protected boolean isDuplicateRequest(User user, Object request) {
        return false; // No duplicate check needed for competition creation requests
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof CompetitionCreationRequest existingCompetitionRequest && updatedRequest instanceof CompetitionCreationRequest updatedCompetitionRequest) {
            existingCompetitionRequest.setCompetitionRequest(updatedCompetitionRequest.getCompetitionRequest());
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        CompetitionRequest competitionRequest = mapperUtils.mapToCompetitionRequest((CompetitionCreationRequest) request);
        log.info("Creating competition {}, by user with email: {}", ((CompetitionCreationRequest) request).getCompetitionRequest().name(), user.getEmail());
        competitionService.createCompetition(request.getCreatedBy(), competitionRequest);
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return Set.of(Role.ADMIN);
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        CompetitionCreationRequest competitionRequest = CompetitionCreationRequest.builder().competitionRequest((CompetitionRequest) request).build();
        competitionRequest.prePersist();
        return competitionRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.mapToCompetitionCreationResponse((CompetitionCreationRequest) request);
    }
}
