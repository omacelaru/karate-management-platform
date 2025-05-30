package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.services.CategoryService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryResource {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get default categories",
            description = "Returns a list of all default categories",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Default categories returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/default")
    public ResponseEntity<Set<CategoryResponse>> getDefaultCategoryResponses() {
        return ResponseEntity.ok(categoryService.getDefaultCategoryResponses());
    }
} 