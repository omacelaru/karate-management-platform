package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_auth_service.models.organizer.Organizer;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final MapperUtils mapperUtils;

    @Transactional
    public void createOrganizer(User user, OrganizerRequest organizerRequest) {
        log.info("Creating organizer for user with email: {}", user.getEmail());
        UserService.applyRolesToUser(user, Role.ORGANIZER);

        Organizer organizer = mapperUtils.mapToOrganizer(organizerRequest);
        organizer.setUser(user);

        organizerRepository.save(organizer);
    }
}
