package ro.unibuc.fmi.karate_management_platform.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.services.RequestService;

@RestController
@RequestMapping("/api/v1/requests/competitions")
@RequiredArgsConstructor
public class RequestCompetitionResource {
    private final RequestService requestService;
}
