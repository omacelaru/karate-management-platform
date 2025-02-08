package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;

@RestController
@RequestMapping("/api/v1/competitions")
@RequiredArgsConstructor
public class CompetitionResource {
    private final CompetitionService competitionService;

    @Operation(summary = "Get all competitions paged", description = "Returns a list of all competitions paged")
    @ApiResponse(responseCode = "200", description = "Competitions returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<CompetitionResponse>> getAllCompetitions(Pageable pageable) {
        return ResponseEntity.ok(competitionService.getAllCompetitions(pageable));
    }
}
