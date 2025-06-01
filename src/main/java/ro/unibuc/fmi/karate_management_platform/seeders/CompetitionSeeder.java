package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import ro.unibuc.fmi.karate_management_platform.services.OrganizerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CompetitionSeeder implements CommandLineRunner {
    private final CompetitionService competitionService;
    private final OrganizerService organizerService;
    private final Random random = new Random();

    private static final List<String> COMPETITION_NAMES = List.of(
        "National Karate Championship",
        "International Karate Cup",
        "Regional Karate Tournament",
        "City Karate Championship",
        "Youth Karate Festival",
        "Elite Karate Competition",
        "Open Karate Championship",
        "Spring Karate Tournament",
        "Summer Karate Cup",
        "Winter Karate Championship"
    );

    private static final List<String> VENUES = List.of(
        "Sports Complex Arena",
        "Olympic Center",
        "City Sports Hall",
        "National Stadium",
        "Regional Sports Center",
        "University Sports Complex",
        "Community Sports Center",
        "Elite Training Center",
        "International Sports Arena",
        "Youth Sports Center"
    );

    @Override
    @Transactional
    public void run(String... args) {
        seedCompetitions();
    }

    private void seedCompetitions() {
        if (!competitionService.getAllCompetitionsForSeeder().isEmpty()) {
            return;
        }

        List<Organizer> organizers = organizerService.getAllOrganizersForSeeder();
        if (organizers.isEmpty()) {
            return;
        }

        // Create competitions with different statuses
        for (int i = 0; i < 10; i++) {
            String name = COMPETITION_NAMES.get(i);
            String venue = VENUES.get(random.nextInt(VENUES.size()));
            LocalDate startDate = LocalDate.now().plusDays(random.nextInt(365));
            Organizer organizer = organizers.get(random.nextInt(organizers.size()));

            CompetitionRequest request = new CompetitionRequest(
                name,
                venue,
                startDate
            );

            competitionService.createCompetition(organizer.getUser(),request);
        }
    }

} 