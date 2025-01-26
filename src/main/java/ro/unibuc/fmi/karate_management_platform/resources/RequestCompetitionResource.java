package ro.unibuc.fmi.karate_management_platform.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.RequestService;

@RestController
@RequestMapping("/api/v1/requests/competitions")
@RequiredArgsConstructor
public class RequestCompetitionResource {
    private final RequestService requestService;

    @SecuredEndpoint
    @PostMapping
    public ResponseEntity<RequestInfoResponseInterface> competitionCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CompetitionRequest competitionRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.COMPETITION_CREATION, competitionRequest));
    }
}
