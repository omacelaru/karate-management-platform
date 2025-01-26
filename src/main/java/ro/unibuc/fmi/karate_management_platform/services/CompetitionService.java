package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final MapperUtils mapperUtils;

    @Transactional
    public void createCompetition(CompetitionRequest competitionRequest) {
        log.info("Creating competition {}", competitionRequest.name());

        Competition competition = mapperUtils.mapToCompetition(competitionRequest);

        competitionRepository.save(competition);
    }
}
