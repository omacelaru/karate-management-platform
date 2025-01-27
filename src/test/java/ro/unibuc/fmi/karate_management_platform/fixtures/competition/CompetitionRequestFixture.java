package ro.unibuc.fmi.karate_management_platform.fixtures.competition;


import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;

import java.time.LocalDate;
import java.util.Set;

public class CompetitionRequestFixture {

    public static CompetitionRequest createDefaultCompetitionRequest() {
        return new CompetitionRequest(
                "Karate Championship",
                "Bucharest",
                LocalDate.now().plusDays(3),
                Set.of(1L, 2L, 3L)
        );
    }

    public static CompetitionRequest createCustomCompetitionRequest(
            String name,
            LocalDate date,
            String location,
            Set<Long> categoriesIds
    ) {
        return new CompetitionRequest(
                name,
                location,
                date,
                categoriesIds
        );
    }
}