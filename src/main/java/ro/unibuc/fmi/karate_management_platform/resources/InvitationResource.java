package ro.unibuc.fmi.karate_management_platform.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.InvitationService;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationResource {
    private final InvitationService invitationService;

    @SecuredEndpoint(roles = {Role.ADMIN})
    @PostMapping()
    public ResponseEntity<String> sendInvitation(
            @AuthenticationPrincipal User user,
            @RequestParam String email
    ) {
        invitationService.sendInvitation(email, user.getId());
        return ResponseEntity.ok("Invitation sent.");
    }

    @GetMapping("/accept")
    public ResponseEntity<String> acceptInvitation(@RequestParam String token) {
        try {
            invitationService.acceptInvitation(token);
            return ResponseEntity.ok("Invitation accepted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

