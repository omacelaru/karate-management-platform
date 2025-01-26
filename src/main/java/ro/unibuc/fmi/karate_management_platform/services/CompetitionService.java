package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final CategoryService categoryService;
    private final MapperUtils mapperUtils;
    private final OrganizerRepository organizerRepository;

    @Transactional
    public void createCompetition(User createdBy, CompetitionRequest competitionRequest) {
        log.info("Creating competition {}", competitionRequest.name());

        Organizer organizer = organizerRepository.findByUserEmail(createdBy.getEmail()).orElseThrow();

        Set<Category> categories = categoryService.getCategoriesByIds(competitionRequest.categoriesIds());

        Competition competition = mapperUtils.mapToCompetition(competitionRequest);
        competition.setCategories(categories);
        competition.setOrganizer(organizer);

        competitionRepository.save(competition);
    }
}
