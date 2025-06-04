package ro.unibuc.fmi.karate_management_platform.dtos.competition.scheduling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledCategory {
    private Long categoryId;
    private int tatamiId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String categoryName;
    private int totalDuration;
} 