package ro.unibuc.fmi.karate_management_platform.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for the request to reset the user's password.
 * This DTO is used to send the new password when a user requests to reset their password.
 * <p>Usage: This DTO is used for sending the new password in a POST request to reset the user's password.</p>
 *
 * @param oldPassword The user's old password.
 * @param newPassword The user's new password.
 */
@Schema(description = "Request to reset the user's password.")
public record ResetPasswordRequest(
        String oldPassword,
        String newPassword
) {
}
