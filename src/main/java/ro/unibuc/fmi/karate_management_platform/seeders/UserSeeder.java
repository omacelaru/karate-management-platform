package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Belt;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_management_platform.models.referee.RefereeLevel;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {
    private final UserService userService;
    private final AthleteService athleteService;
    private final CoachService coachService;
    private final RefereeService refereeService;
    private final OrganizerService organizerService;

    private static final String PASSWORD = "Password123!";
    private final Random random = new Random();

    private static final List<String> NATIONALITIES = List.of("USA", "UK", "Canada", "Germany", "France", "Spain", "Italy", "Brazil", "Japan", "Australia");

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
    }

    private void seedUsers() {
        // Dacă există deja utilizatori, nu se mai face seeding
        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        List<Athlete> seededAthletes = new ArrayList<>();
        List<Coach> seededCoaches = new ArrayList<>();

        seededAthletes.add(createAthlete("john.athlete.coach1@karate.com", "John", "Doe"));
        seededAthletes.add(createAthlete("jane.athlete.coach1@karate.com", "Jane", "Smith"));
        seededAthletes.add(createAthlete("alex.athlete.coach1@karate.com", "Alex", "Johnson"));
        seededAthletes.add(createAthlete("emma.athlete.coach2@karate.com", "Emma", "Brown"));
        seededAthletes.add(createAthlete("dore.athlete.coach2@karate.com", "Dore", "Smith"));
        seededAthletes.add(createAthlete("enif.athlete.coach2@karate.com", "Enif", "Red"));

        seededCoaches.add(createCoach("mike.coach1@karate.com", "Mike", "Taylor"));
        seededCoaches.add(createCoach("susan.coach2@karate.com", "Susan", "Anderson"));
        seededCoaches.add(createCoach("don.coach3@karate.com", "Don", "White"));
        seededCoaches.add(createCoach("wally.coach4@karate.com", "Wally", "Green"));

        createGenericUser("mark.referee1@karate.com", "Mark", "Wilson", Role.REFEREE, createRefereeRequest());
        createGenericUser("lucy.referee2@karate.com", "Lucy", "Evans", Role.REFEREE, createRefereeRequest());
        createGenericUser("miles.referee3@karate.com", "Miles", "Brown", Role.REFEREE, createRefereeRequest());
        createGenericUser("ronald.referee4@karate.com", "Ronald", "Smith", Role.REFEREE, createRefereeRequest());

        createGenericUser("chris.organizer1@karate.com", "Chris", "Miller", Role.ORGANIZER, createOrganizerRequest());
        createGenericUser("natalie.organizer2@karate.com", "Natalie", "Harris", Role.ORGANIZER, createOrganizerRequest());

        createAdminUser();

        for (Athlete athlete : seededAthletes) {
            Coach assignedCoach = seededCoaches.get(random.nextInt(seededCoaches.size()));
            athlete.setCoaches(new HashSet<>(List.of(assignedCoach)));
            athleteService.updateAthlete(athlete);
        }
    }

    private void createAdminUser() {
        User user = userService.createUser("admin", PASSWORD);
        updateUser("admin", "admin", "admin");
        user.addRole(Role.ADMIN);
        userService.updateUser(user);
    }

    private Athlete createAthlete(String email, String firstName, String lastName) {
        User user = userService.createUser(email, PASSWORD);
        updateUser(email, firstName, lastName);
        AthleteRequest athleteRequest = createAthleteRequest();
        return athleteService.createAthlete(user, athleteRequest);
    }

    private Coach createCoach(String email, String firstName, String lastName) {
        User user = userService.createUser(email, PASSWORD);
        updateUser(email, firstName, lastName);
        CoachRequest coachRequest = createCoachRequest();
        return coachService.createCoach(user, coachRequest);
    }

    private void createGenericUser(String email, String firstName, String lastName, Role role, Object request) {
        User user = userService.createUser(email, PASSWORD);
        updateUser(email, firstName, lastName);
        if (role == Role.REFEREE) {
            refereeService.createReferee(user, (RefereeRequest) request);
        } else if (role == Role.ORGANIZER) {
            organizerService.createOrganizer(user, (OrganizerRequest) request);
        }
    }

    private void updateUser(String email, String firstName, String lastName) {
        User user = userService.findByEmail(email).orElseThrow();
        String nationality = NATIONALITIES.get(random.nextInt(NATIONALITIES.size()));
        LocalDate birthDate = getRandomBirthDate();
        Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
        String profilePictureUrl = "https://randomuser.me/api/portraits/" + (gender == Gender.MALE ? "men" : "women")
                + "/" + random.nextInt(100) + ".jpg";

        UserDetailsRequest userDetailsRequest = new UserDetailsRequest(lastName, firstName, nationality, birthDate, gender, profilePictureUrl);
        userService.updateMe(user, userDetailsRequest);
    }

    private AthleteRequest createAthleteRequest() {
        return new AthleteRequest(
                (long) (random.nextInt(10) + 1),
                random.nextInt(200 - 50) + 50,
                random.nextInt(150 - 20) + 20,
                Belt.GREEN
        );
    }

    private CoachRequest createCoachRequest() {
        return new CoachRequest(new LicenseInfo(generateLicenseSeries(), generateLicenseNumber()));
    }

    private RefereeRequest createRefereeRequest() {
        return new RefereeRequest(
                new LicenseInfo(generateLicenseSeries(), generateLicenseNumber()),
                RefereeLevel.values()[random.nextInt(RefereeLevel.values().length)]
        );
    }

    private OrganizerRequest createOrganizerRequest() {
        return new OrganizerRequest(new TaxRegistrationNumber(generateTaxNumber()));
    }

    private LocalDate getRandomBirthDate() {
        int year = random.nextInt(2010 - 1980 + 1) + 1980;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return LocalDate.of(year, month, day);
    }

    private String generateLicenseSeries() {
        return "LI" + (char) (random.nextInt(26) + 'A');
    }

    private String generateLicenseNumber() {
        return String.format("%06d", random.nextInt(999999));
    }

    private String generateTaxNumber() {
        return String.format("%08d", random.nextInt(99999999));
    }
}
