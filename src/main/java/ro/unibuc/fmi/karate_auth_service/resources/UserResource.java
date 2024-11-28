package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.UserService;

@RestController
@RequestMapping("/api/v1//users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @Operation(summary = "Get logged in user information",
            description = "Returns information about the logged in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information returned successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @SecuredEndpoint
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(userService.getMe());
    }
}
