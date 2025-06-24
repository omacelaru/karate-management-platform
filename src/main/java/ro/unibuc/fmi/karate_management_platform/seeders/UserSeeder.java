package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Belt;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.club.Club;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_management_platform.models.referee.RefereeLevel;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.*;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {
    private final UserService userService;
    private final AthleteService athleteService;
    private final CoachService coachService;
    private final RefereeService refereeService;
    private final OrganizerService organizerService;
    private final ClubService clubService;

    private static final String PASSWORD = "Password123!";
    private final Random random = new Random();

    private static final List<String> NATIONALITIES = List.of(
        "Romania", "USA", "UK", "Canada", "Germany", "France", "Spain", 
        "Italy", "Brazil", "Japan", "Australia", "China", "Russia", 
        "South Korea", "Netherlands", "Sweden", "Norway", "Denmark", 
        "Finland", "Poland", "Ukraine", "Turkey", "Greece", "Portugal"
    );

    private static final List<String> FIRST_NAMES = List.of(
        "John", "Jane", "Michael", "Emma", "David", "Sarah", "James", "Maria",
        "Robert", "Anna", "William", "Sophia", "Daniel", "Olivia", "Matthew",
        "Isabella", "Joseph", "Mia", "Andrew", "Charlotte", "Thomas", "Amelia",
        "Joshua", "Harper", "Ryan", "Evelyn", "Nicholas", "Abigail", "Tyler",
        "Emily", "Alexander", "Elizabeth", "Nathan", "Sofia", "Samuel", "Avery"
    );

    private static final List<String> LAST_NAMES = List.of(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
        "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez",
        "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
        "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark",
        "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King",
        "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores"
    );

    private static final List<String> CLUB_NAMES = List.of(
        "Dragon Karate Club", "Samurai Dojo", "Tiger's Den", "Phoenix Martial Arts",
        "Black Belt Academy", "Warrior's Path", "Zen Karate Center", "Eagle's Nest Dojo",
        "Golden Dragon", "Silver Tiger", "Red Phoenix", "Blue Wave",
        "White Crane", "Black Panther", "Green Dragon", "Yellow Tiger"
    );

    private static final List<String> CITIES = List.of(
        "București", "Cluj-Napoca", "Timișoara", "Iași", "Constanța",
        "Brașov", "Sibiu", "Oradea", "Craiova", "Galați",
        "Ploiești", "Brăila", "Pitești", "Bacău", "Arad"
    );

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
    }

    private void seedUsers() {
        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        // Create admin first
        User admin = createAdminUser();

        // Create clubs (8 clubs)
        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String clubName = CLUB_NAMES.get(i);
            String acronym = clubName.replaceAll(" ", "").substring(0, 3).toUpperCase();
            String city = CITIES.get(random.nextInt(CITIES.size()));
            String address = "Strada " + (i + 1) + ", Nr. " + (random.nextInt(100) + 1);
            String phone = "07" + String.format("%08d", random.nextInt(100000000));
            String email = "contact@" + clubName.toLowerCase().replace(" ", "") + ".com";
            
            ClubRequest clubRequest = new ClubRequest(clubName,acronym, city, address, phone, email);
            clubs.add(clubService.createClubForSeeder(clubRequest));
        }

        // Create coaches (10 coaches)
        List<Coach> coaches = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String email = String.format("coach%d@karate.com", i + 1);
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
            Coach coach = createCoach(email, firstName, lastName);
            
            // Assign coach to a random club
            Club randomClub = clubs.get(random.nextInt(clubs.size()));
            coach.setClub(randomClub);
            coachService.updateCoach(coach);
            
            coaches.add(coach);
        }

        // Create athletes (50 athletes)
        List<Athlete> athletes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String email = String.format("athlete%d@karate.com", i + 1);
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
            Athlete athlete = createAthlete(email, firstName, lastName);
            
            // Assign athlete to a coach
            Coach randomCoach = coaches.get(random.nextInt(coaches.size()));
            athlete.getCoaches().add(randomCoach);
            athleteService.updateAthlete(athlete);
            
            athletes.add(athlete);
        }

        // Create referees (15 referees)
        for (int i = 0; i < 15; i++) {
            String email = String.format("referee%d@karate.com", i + 1);
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
            createGenericUser(email, firstName, lastName, Role.REFEREE, createRefereeRequest());
        }

        // Create organizers (8 organizers)
        for (int i = 0; i < 8; i++) {
            String email = String.format("organizer%d@karate.com", i + 1);
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
            createGenericUser(email, firstName, lastName, Role.ORGANIZER, createOrganizerRequest());
        }
    }

    private User createAdminUser() {
        User user = userService.createUser(new AuthRequest("admin@admin.com", PASSWORD, "en"));
        updateUser("admin@admin.com", "Admin", "Admin");
        user.addRole(Role.ADMIN);
        userService.updateUser(user);
        return user;
    }

    private Athlete createAthlete(String email, String firstName, String lastName) {
        User user = userService.createUser(new AuthRequest(email, PASSWORD, "en"));
        updateUser(email, firstName, lastName);
        AthleteRequest athleteRequest = createAthleteRequest();
        return athleteService.createAthlete(user, athleteRequest);
    }

    private Coach createCoach(String email, String firstName, String lastName) {
        User user = userService.createUser(new AuthRequest(email, PASSWORD, "en"));
        updateUser(email, firstName, lastName);
        CoachRequest coachRequest = createCoachRequest();
        return coachService.createCoach(user, coachRequest);
    }

    private void createGenericUser(String email, String firstName, String lastName, Role role, Object request) {
        User user = userService.createUser(new AuthRequest(email, PASSWORD, "en"));
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
                Belt.values()[random.nextInt(Belt.values().length)]
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
