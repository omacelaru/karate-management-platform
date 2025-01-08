package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.repositories.RefereeRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final MapperUtils mapperUtils;
}
