package ro.unibuc.fmi.karate_management_platform.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Error response")
public record ApiError(
        @Schema(description = "HTTP status") Integer status,
        @Schema(description = "Error type") String error,
        @Schema(description = "Error messages") List<String> message,
        @Schema(description = "Path where the error occurred") String path,
        @Schema(description = "Timestamp when the error occurred") String timestamp
) {
    public ApiError(Integer status, String error, String singleMessage, String path, String timestamp) {
        this(status, error, List.of(singleMessage), path, timestamp);
    }
}
