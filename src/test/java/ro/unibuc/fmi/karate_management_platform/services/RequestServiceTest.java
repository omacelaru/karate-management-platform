package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.factories.RequestTypeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.strategies.requestType.RequestTypeStrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    private RequestService requestService;
    private RequestTypeStrategyFactory strategyFactory;
    private RequestTypeStrategy requestTypeStrategy;

    @BeforeEach
    void setUp() {
        strategyFactory = mock(RequestTypeStrategyFactory.class);
        requestTypeStrategy = mock(RequestTypeStrategy.class);
        requestService = new RequestService(strategyFactory);
    }

    @Test
    void getRequestsMadeByMe_shouldReturnRequests() {
        User user = new User();
        Pageable pageable = mock(Pageable.class);
        RequestType requestType = RequestType.COACH_CREATION;
        Page expectedPage = mock(Page.class);

        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.getRequestsMadeByMe(user, pageable)).thenReturn(expectedPage);

        Page<? extends RequestInfoResponseInterface> result = requestService.getRequestsMadeByMe(user, pageable, requestType);

        assertThat(result).isEqualTo(expectedPage);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).getRequestsMadeByMe(user, pageable);
    }

    @Test
    void getRequestsAssignedToMe_shouldReturnRequests() {
        User user = new User();
        Pageable pageable = mock(Pageable.class);
        RequestType requestType = RequestType.COACH_CREATION;
        Page expectedPage = mock(Page.class);

        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.getRequestsAssignedToMe(user, pageable)).thenReturn(expectedPage);

        Page<? extends RequestInfoResponseInterface> result = requestService.getRequestsAssignedToMe(user, pageable, requestType);

        assertThat(result).isEqualTo(expectedPage);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).getRequestsAssignedToMe(user, pageable);
    }

    @Test
    void updateRequestStatus_shouldUpdateStatus() {
        User user = new User();
        Long requestId = 1L;
        RequestStatus status = RequestStatus.ACCEPTED;
        RequestType requestType = RequestType.COACH_CREATION;
        CoachCreationResponse response = mock(CoachCreationResponse.class);

        when(strategyFactory.getRequestType(requestId)).thenReturn(requestType);
        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.updateRequestStatus(user, requestId, status)).thenReturn(response);

        RequestInfoResponseInterface result = requestService.updateRequestStatus(user, requestId, status);

        assertThat(result).isEqualTo(response);
        verify(strategyFactory).getRequestType(requestId);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).updateRequestStatus(user, requestId, status);
    }

    @Test
    void createRequest_shouldCreateRequest() {
        User user = new User();
        RequestType requestType = RequestType.COACH_CREATION;
        Object request = new Object();
        CoachCreationResponse response = mock(CoachCreationResponse.class);

        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.createRequest(user, request)).thenReturn(response);

        RequestInfoResponseInterface result = requestService.createRequest(user, requestType, request);

        assertThat(result).isEqualTo(response);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).createRequest(user, request);
    }

    @Test
    void editRequest_shouldEditRequest() {
        User user = new User();
        RequestType requestType = RequestType.COACH_CREATION;
        Object request = new Object();
        CoachCreationResponse response = mock(CoachCreationResponse.class);

        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.editRequest(user, request)).thenReturn(response);

        RequestInfoResponseInterface result = requestService.editRequest(user, requestType, request);

        assertThat(result).isEqualTo(response);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).editRequest(user, request);
    }

    @Test
    void revokeRequest_shouldRevokeRequest() {
        User user = new User();
        Long requestId = 1L;
        RequestType requestType = RequestType.COACH_CREATION;
        CoachCreationResponse response = mock(CoachCreationResponse.class);

        when(strategyFactory.getRequestType(requestId)).thenReturn(requestType);
        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.revokeRequest(user, requestId)).thenReturn(response);

        RequestInfoResponseInterface result = requestService.revokeRequest(user, requestId);

        assertThat(result).isEqualTo(response);
        verify(strategyFactory).getRequestType(requestId);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).revokeRequest(user, requestId);
    }

    @Test
    void activateRequest_shouldActivateRequest() {
        User user = new User();
        Long requestId = 1L;
        RequestType requestType = RequestType.COACH_CREATION;
        CoachCreationResponse response = mock(CoachCreationResponse.class);

        when(strategyFactory.getRequestType(requestId)).thenReturn(requestType);
        when(strategyFactory.getStrategy(requestType)).thenReturn(requestTypeStrategy);
        when(requestTypeStrategy.activateRequest(user, requestId)).thenReturn(response);

        RequestInfoResponseInterface result = requestService.activateRequest(user, requestId);

        assertThat(result).isEqualTo(response);
        verify(strategyFactory).getRequestType(requestId);
        verify(strategyFactory).getStrategy(requestType);
        verify(requestTypeStrategy).activateRequest(user, requestId);
    }
}