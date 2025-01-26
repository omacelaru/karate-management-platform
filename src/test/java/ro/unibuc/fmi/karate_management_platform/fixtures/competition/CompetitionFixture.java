package ro.unibuc.fmi.karate_management_platform.fixtures.competition;


import ro.unibuc.fmi.karate_management_platform.fixtures.competition.category.CategoryFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.organizer.OrganizerFixture;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;

import java.time.LocalDate;
import java.util.Set;

public class CompetitionFixture {

    public static Competition createDefaultCompetition() {
        Organizer organizer = OrganizerFixture.createDefaultOrganizer();
        return Competition.builder()
                .id(1L)
                .name("Karate Championship")
                .date(LocalDate.now().minusDays(1))
                .location("Bucharest")
                .categories(Set.of(CategoryFixture.createDefaultCategory()))
                .organizer(organizer)
                .build();
    }

    public static Competition createCustomCompetition(
            Long id,
            String name,
            LocalDate date,
            String location,
            Set<Category> categories,
            Organizer organizer
    ) {
        return Competition.builder()
                .id(id)
                .name(name)
                .date(date)
                .location(location)
                .categories(categories)
                .organizer(organizer)
                .build();
    }
}