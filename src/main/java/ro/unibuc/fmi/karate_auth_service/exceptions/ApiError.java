package ro.unibuc.fmi.karate_auth_service.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response")
public record ApiError(
        @Schema(description = "HTTP status") Integer status,
        @Schema(description = "Error type") String error,
        @Schema(description = "Error message") String message,
        @Schema(description = "Path where the error occurred") String path,
        @Schema(description = "Timestamp when the error occurred") String timestamp
) {
}
