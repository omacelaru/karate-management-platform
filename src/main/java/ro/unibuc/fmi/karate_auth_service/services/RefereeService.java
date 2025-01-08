package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.models.referee.Referee;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.RefereeRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final MapperUtils mapperUtils;

    @Transactional
    public void createReferee(User user, @Valid RefereeRequest refereeRequest) {
        log.info("Creating referee for user with email: {}", user.getEmail());
        UserService.applyRolesToUser(user, Role.REFEREE);

        Referee referee = mapperUtils.mapToReferee(refereeRequest);
        referee.setUser(user);

        refereeRepository.save(referee);
    }
}
