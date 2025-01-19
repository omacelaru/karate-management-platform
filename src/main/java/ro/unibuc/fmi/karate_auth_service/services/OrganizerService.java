package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
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

    public Page<OrganizerResponse> getAllOrganizers(Pageable pageable) {
        log.info("Getting all organizers");
        return organizerRepository.findAll(pageable).map(mapperUtils::mapToOrganizerResponse);
    }

    public OrganizerResponse getMe(User user) {
        String email = user.getEmail();

        log.info("Getting organizer with email: {}", email);
        //TODO - fix incomplete profile exception
        Organizer organizer = organizerRepository.findByUserEmail(email).orElseThrow(IncompleteProfileException::new);

        return mapperUtils.mapToOrganizerResponse(organizer);
    }

    @Transactional
    public void createOrganizer(User user, OrganizerRequest organizerRequest) {
        log.info("Creating organizer for user with email: {}", user.getEmail());
        UserService.applyRolesToUser(user, Role.ORGANIZER);

        Organizer organizer = mapperUtils.mapToOrganizer(organizerRequest);
        organizer.setUser(user);

        organizerRepository.save(organizer);
    }


}
