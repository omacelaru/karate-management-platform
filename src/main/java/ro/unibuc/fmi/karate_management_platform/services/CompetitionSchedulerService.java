package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionSchedulerService {
    private final CompetitionService competitionService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void closeRegistrationsForUpcomingCompetitions() {
        log.info("Starting scheduled task to close registrations for upcoming competitions");
        
        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        List<Competition> competitions = competitionService.getAllCompetitionsForSeeder();
        
        competitions.stream()
            .filter(competition -> competition.getDate().equals(twoDaysFromNow))
            .filter(Competition::isRegistrationOpen)
            .forEach(competition -> {
                log.info("Closing registrations for competition: {} (ID: {})", competition.getName(), competition.getId());
                competitionService.toggleRegistrationStatus(competition.getOrganizer().getUser(), competition.getId(), false);
            });
            
        log.info("Finished scheduled task to close registrations for upcoming competitions");
    }
} 