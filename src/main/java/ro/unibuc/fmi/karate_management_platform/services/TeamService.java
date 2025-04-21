package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final AthleteRepository athleteRepository;
    private final MapperUtils mapperUtils;

    public boolean existsByMembersIds(Set<Long> athletesIds) {
        log.info("Checking if team exists by members ids: {}", athletesIds);
        return teamRepository.existsByAthletes_IdIn(athletesIds);
    }

    public Team createTeam(Set<Long> athletesIds) {
        log.info("Creating team with athletes ids: {}", athletesIds);
        List<Athlete> athletes = athleteRepository.findAllById(athletesIds);
        Set<Athlete> athletesSet = new HashSet<>(athletes);
        Team team = Team.builder()
                .athletes(athletesSet)
                .build();
        return teamRepository.save(team);
    }

    public Team findTeamByMembersIds(Set<Long> athletesIds) {
        log.info("Finding team by members ids: {}", athletesIds);
        return teamRepository.findByAthletes_IdIn(athletesIds).orElseThrow(() -> {
            log.error("Team with members ids {} not found", athletesIds);
            return new IllegalArgumentException("Team with members ids " + athletesIds + " not found");
        });
    }
}
