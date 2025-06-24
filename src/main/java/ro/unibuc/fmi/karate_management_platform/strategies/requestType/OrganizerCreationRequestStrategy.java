package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.organizer.OrganizerCreationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.OrganizerService;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Component
public class OrganizerCreationRequestStrategy extends AbstractUserCreationRequestTypeStrategy {
    @Getter
    private final RequestType requestType = RequestType.ORGANIZER_CREATION;
    private final OrganizerService organizerService;

    @Autowired
    public OrganizerCreationRequestStrategy(MapperUtils mapperUtils, RequestInfoRepositoryFactory repositoryFactory, RequestScopeStrategyFactory scopeStrategyFactory, OrganizerService organizerService) {
        super(mapperUtils, repositoryFactory, scopeStrategyFactory);
        this.organizerService = organizerService;
    }

    @Override
    protected boolean isUserAlreadyHasRequestedRole(User user) {
        return user.getRoles().contains(Role.ORGANIZER);
    }

    @Override
    protected void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest) {
        if (existingRequest instanceof OrganizerCreationRequest existingOrganizerRequest && updatedRequest instanceof OrganizerCreationRequest updatedOrganizerRequest) {
            existingOrganizerRequest.setOrganizerRequest(updatedOrganizerRequest.getOrganizerRequest());
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    @Override
    protected void handleAcceptedRequest(User user, RequestInfo request) {
        User userToBePromoteToOrganizer = request.getCreatedBy();
        OrganizerRequest organizerRequest = mapperUtils.mapToOrganizerRequest((OrganizerCreationRequest) request);
        log.info("Creating organizer for user with email: {}", user.getEmail());
        organizerService.createOrganizer(userToBePromoteToOrganizer, organizerRequest);
    }

    @Override
    protected Set<?> handleApprovers(User user, Object request) {
        return Set.of(Role.ADMIN);
    }

    @Override
    protected RequestInfo mapToEntity(User user, Object request) {
        OrganizerCreationRequest organizerCreationRequest = mapperUtils.mapToOrganizerCreationRequest((OrganizerRequest) request);
        organizerCreationRequest.prePersist();
        return organizerCreationRequest;
    }

    @Override
    protected RequestInfoResponseInterface mapToResponse(RequestInfo request) {
        return mapperUtils.mapToOrganizerCreationResponse((OrganizerCreationRequest) request);
    }
}
