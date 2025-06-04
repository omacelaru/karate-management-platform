package ro.unibuc.fmi.karate_management_platform.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.scheduling.ScheduledCategory;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionSchedulingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/competitions")
@RequiredArgsConstructor
public class CompetitionSchedulingController {

    private final CompetitionSchedulingService competitionSchedulingService;

    @GetMapping("/{competitionId}/scheduling")
    public ResponseEntity<List<ScheduledCategory>> getCompetitionSchedule(@PathVariable Long competitionId) {
        log.info("Getting schedule for competition {}", competitionId);
        List<ScheduledCategory> schedule = competitionSchedulingService.scheduleCategories(competitionId);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{competitionId}/scheduling-graph-coloring")
    public ResponseEntity<List<ScheduledCategory>> getCompetitionScheduleGraphColoring(@PathVariable Long competitionId) {
        log.info("Getting schedule (graph coloring) for competition {}", competitionId);
        List<ScheduledCategory> schedule = competitionSchedulingService.scheduleCategoriesGraphColoring(competitionId);
        return ResponseEntity.ok(schedule);
    }
} 