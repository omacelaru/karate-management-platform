package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Component
@Getter
@RequiredArgsConstructor
public class CoachCreationRequestStrategy implements RequestTypeStrategy {
    private final RequestType requestType = RequestType.COACH_CREATION;
    private final CoachCreationRequestRepository coachCreationRequestRepository;
    private final MapperUtils mapperUtils;

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        return coachCreationRequestRepository.findAllByCreatedById(user.getId(), pageable).map(mapperUtils::mapToCoachCreationResponse);
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable) {
        return coachCreationRequestRepository.findAllByApproverRolesIn(user.getRoles(), pageable).map(mapperUtils::mapToCoachCreationResponse);
    }
}
