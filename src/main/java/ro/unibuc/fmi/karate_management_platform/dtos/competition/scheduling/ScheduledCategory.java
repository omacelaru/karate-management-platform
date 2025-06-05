package ro.unibuc.fmi.karate_management_platform.dtos.competition.scheduling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledCategory {
    private CategoryResponse category;
    private int tatamiId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String categoryName;
    private int totalDuration;
} 