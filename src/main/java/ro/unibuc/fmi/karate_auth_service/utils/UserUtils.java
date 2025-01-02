package ro.unibuc.fmi.karate_auth_service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.UserRepository;

@Component
public class UserUtils {

    private static UserRepository userRepository;

    @Autowired
    public UserUtils(UserRepository userRepository) {
        UserUtils.userRepository = userRepository;
    }

    public static User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public static User getCurrentUserIfHasRole(Role role) {
        User user = getCurrentUser();
        if (hasRole(user, role)) {
            return user;
        }
        throw new IllegalArgumentException("Invalid role");
    }

    public static boolean hasRole(User user, Role role) {
        return user.getRoles().contains(role);
    }

}
