package ro.unibuc.fmi.karate_management_platform.fixtures.organizer;


import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserFixture;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

public class OrganizerFixture {

    public static Organizer createDefaultOrganizer() {
        User user = UserFixture.createDefaultUser();
        Organizer organizer = new Organizer();
        organizer.setId(1L);
        organizer.setUser(user);
        return organizer;
    }

}