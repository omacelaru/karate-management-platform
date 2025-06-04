package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.scheduling.ScheduledCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryScheduling;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.repositories.CategorySchedulingRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionSchedulingService {

    private final CategorySchedulingRepository categorySchedulingRepository;
    private final CompetitionRepository competitionRepository;
    
    private static final int NUMBER_OF_TATAMIS = 3;
    private static final LocalTime COMPETITION_START_TIME = LocalTime.of(9, 0);
    private final MapperUtils mapperUtils;

    public List<CategoryScheduling> findAllByCompetitionId(Long competitionId) {
        log.info("Fetching all category schedules for competition {}", competitionId);
        return categorySchedulingRepository.findAllByCompetitionId(competitionId);

    }

    @Transactional
    public List<ScheduledCategory> scheduleCategories(Long competitionId) {
        log.info("Starting category scheduling for competition {}", competitionId);
        
        // Get competition and categories
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        Set<Category> categories = competition.getCategories().stream()
                .filter(category -> category instanceof IndividualCategory || category instanceof TeamCategory)
                .filter(category -> !category.getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .collect(Collectors.toSet());
        
        // Delete any existing schedule
        categorySchedulingRepository.deleteByCompetitionId(competitionId);
        
        // Create a map of athlete IDs to category IDs to detect conflicts
        Map<Long, Set<Long>> athleteToCategories = new HashMap<>();
        
        // Calculate total duration for each category and build athlete-category relationships
        Map<Long, Integer> categoryDurations = new HashMap<>();
        Map<Long, String> categoryNames = new HashMap<>();
        
        for (Category category : categories) {
            int totalDuration = category.getParticipations().stream()
                    .mapToInt(CategoryParticipation::getDurationMinutes)
                    .sum();
            
            categoryDurations.put(category.getId(), totalDuration);
            categoryNames.put(category.getId(), buildCategoryName(category));
            
            // Build athlete-category relationships
            category.getParticipations().forEach(participation -> {
                if (participation instanceof IndividualCategoryParticipation) {
                    Long athleteId = ((IndividualCategoryParticipation) participation).getAthlete().getId();
                    athleteToCategories.computeIfAbsent(athleteId, k -> new HashSet<>()).add(category.getId());
                } else if (participation instanceof TeamCategoryParticipation) {
                    ((TeamCategoryParticipation) participation).getTeam().getAthletes().forEach(athlete -> {
                        Long athleteId = athlete.getId();
                        athleteToCategories.computeIfAbsent(athleteId, k -> new HashSet<>()).add(category.getId());
                    });
                }
            });
        }
        
        // Sort categories by duration (descending)
        List<Long> sortedCategoryIds = new ArrayList<>(categoryDurations.keySet());
        sortedCategoryIds.sort((a, b) -> categoryDurations.get(b).compareTo(categoryDurations.get(a)));
        
        // Initialize scheduling data structures
        List<ScheduledCategory> scheduledCategories = new ArrayList<>();
        Map<Integer, LocalTime> tatamiEndTimes = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_TATAMIS; i++) {
            tatamiEndTimes.put(i, COMPETITION_START_TIME);
        }
        
        // Schedule categories using greedy approach
        for (Long categoryId : sortedCategoryIds) {
            Category category = categories.stream()
                    .filter(c -> c.getId().equals(categoryId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
                    
            int duration = categoryDurations.get(categoryId);
            LocalTime earliestStartTime = null;
            int selectedTatami = -1;
            
            // Find the earliest possible start time across all tatamis
            for (int tatami = 1; tatami <= NUMBER_OF_TATAMIS; tatami++) {
                LocalTime tatamiEndTime = tatamiEndTimes.get(tatami);
                boolean hasConflict = false;
                
                // Check for conflicts with already scheduled categories
                for (ScheduledCategory scheduled : scheduledCategories) {
                    if (scheduled.getTatamiId() == tatami) {
                        // Check if this category would overlap with the scheduled one
                        if (!(tatamiEndTime.isBefore(scheduled.getStartTime()) || 
                              tatamiEndTime.plusMinutes(duration).isAfter(scheduled.getEndTime()))) {
                            hasConflict = true;
                            break;
                        }
                    }
                }
                
                if (!hasConflict && (earliestStartTime == null || tatamiEndTime.isBefore(earliestStartTime))) {
                    earliestStartTime = tatamiEndTime;
                    selectedTatami = tatami;
                }
            }
            
            if (selectedTatami != -1) {
                LocalTime endTime = earliestStartTime.plusMinutes(duration);
                tatamiEndTimes.put(selectedTatami, endTime);
                
                // Create and save the scheduling
                CategoryScheduling scheduling = CategoryScheduling.builder()
                        .category(category)
                        .competition(competition)
                        .tatamiId(selectedTatami)
                        .startTime(earliestStartTime)
                        .endTime(endTime)
                        .build();
                
                categorySchedulingRepository.save(scheduling);
                
                scheduledCategories.add(ScheduledCategory.builder()
                        .category(mapperUtils.mapCategory(category))
                        .tatamiId(selectedTatami)
                        .startTime(earliestStartTime)
                        .endTime(endTime)
                        .categoryName(categoryNames.get(categoryId))
                        .totalDuration(duration)
                        .build());
            }
        }
        
        log.info("Completed scheduling {} categories", scheduledCategories.size());
        return scheduledCategories;
    }
    
    @Transactional
    public List<ScheduledCategory> scheduleCategoriesGraphColoring(Long competitionId) {
        log.info("Starting category scheduling (graph coloring) for competition {}", competitionId);

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        Set<Category> categories = competition.getCategories().stream()
                .filter(category -> category instanceof IndividualCategory || category instanceof TeamCategory)
                .filter(category -> !category.getParticipations().stream()
                        .filter(participation -> participation.getCompetition().getId().equals(competitionId))
                        .collect(Collectors.toSet()).isEmpty())
                .collect(Collectors.toSet());

        // Delete any existing schedule
        categorySchedulingRepository.deleteByCompetitionId(competitionId);

        // Build conflict graph: categoryId -> set of conflicting categoryIds
        Map<Long, Set<Long>> conflictGraph = new HashMap<>();
        Map<Long, Set<Long>> categoryToAthletes = new HashMap<>();
        Map<Long, Integer> categoryDurations = new HashMap<>();
        Map<Long, String> categoryNames = new HashMap<>();

        for (Category category : categories) {
            Set<Long> athleteIds = new HashSet<>();
            int totalDuration = category.getParticipations().stream()
                    .mapToInt(CategoryParticipation::getDurationMinutes)
                    .sum();
            categoryDurations.put(category.getId(), totalDuration);
            categoryNames.put(category.getId(), buildCategoryName(category));

            category.getParticipations().forEach(participation -> {
                if (participation instanceof IndividualCategoryParticipation) {
                    athleteIds.add(((IndividualCategoryParticipation) participation).getAthlete().getId());
                } else if (participation instanceof TeamCategoryParticipation) {
                    ((TeamCategoryParticipation) participation).getTeam().getAthletes().forEach(athlete -> {
                        athleteIds.add(athlete.getId());
                    });
                }
            });
            categoryToAthletes.put(category.getId(), athleteIds);
        }

        // Build conflict edges
        for (Long cat1 : categoryToAthletes.keySet()) {
            for (Long cat2 : categoryToAthletes.keySet()) {
                if (!cat1.equals(cat2)) {
                    Set<Long> intersection = new HashSet<>(categoryToAthletes.get(cat1));
                    intersection.retainAll(categoryToAthletes.get(cat2));
                    if (!intersection.isEmpty()) {
                        conflictGraph.computeIfAbsent(cat1, k -> new HashSet<>()).add(cat2);
                    }
                }
            }
        }

        // Graph coloring (greedy)
        Map<Long, Integer> categoryToTatami = new HashMap<>();
        int maxTatami = NUMBER_OF_TATAMIS;
        for (Long categoryId : categoryToAthletes.keySet()) {
            Set<Integer> usedTatamis = new HashSet<>();
            Set<Long> conflicts = conflictGraph.getOrDefault(categoryId, Collections.emptySet());
            for (Long conflictCat : conflicts) {
                if (categoryToTatami.containsKey(conflictCat)) {
                    usedTatamis.add(categoryToTatami.get(conflictCat));
                }
            }
            // Assign the lowest available tatami
            int tatami = 1;
            while (usedTatamis.contains(tatami) && tatami <= maxTatami) {
                tatami++;
            }
            if (tatami > maxTatami) {
                tatami = 1; // fallback, should not happen if enough tatamis
            }
            categoryToTatami.put(categoryId, tatami);
        }

        // Schedule start/end times per tatami (sequential on each tatami)
        Map<Integer, LocalTime> tatamiEndTimes = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_TATAMIS; i++) {
            tatamiEndTimes.put(i, COMPETITION_START_TIME);
        }
        List<ScheduledCategory> scheduledCategories = new ArrayList<>();
        // Sort by tatami, then by duration desc (optional)
        List<Long> sortedCategoryIds = new ArrayList<>(categoryToTatami.keySet());
        sortedCategoryIds.sort(Comparator.comparing(categoryToTatami::get).thenComparing((id1, id2) -> categoryDurations.get(id2) - categoryDurations.get(id1)));
        for (Long categoryId : sortedCategoryIds) {
            int tatami = categoryToTatami.get(categoryId);
            int duration = categoryDurations.get(categoryId);
            LocalTime startTime = tatamiEndTimes.get(tatami);
            LocalTime endTime = startTime.plusMinutes(duration);
            tatamiEndTimes.put(tatami, endTime);

            Category category = categories.stream()
                    .filter(c -> c.getId().equals(categoryId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            CategoryScheduling scheduling = CategoryScheduling.builder()
                    .category(category)
                    .competition(competition)
                    .tatamiId(tatami)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
            categorySchedulingRepository.save(scheduling);

            scheduledCategories.add(ScheduledCategory.builder()
                    .category(mapperUtils.mapCategory(category))
                    .tatamiId(tatami)
                    .startTime(startTime)
                    .endTime(endTime)
                    .categoryName(categoryNames.get(categoryId))
                    .totalDuration(duration)
                    .build());
        }
        log.info("Completed graph coloring scheduling for {} categories", scheduledCategories.size());
        return scheduledCategories;
    }
    
    private String buildCategoryName(Category category) {
        StringBuilder name = new StringBuilder();

        switch (category) {
            case KataIndividualCategory kataIndividualCategory -> name.append("Kata Individual");
            case KumiteIndividualCategory kumiteIndividualCategory -> name.append("Kumite Individual");
            case KataTeamCategory kataTeamCategory -> name.append("Kata Team");
            case KumiteTeamCategory kumiteTeamCategory -> name.append("Kumite Team");
            case null, default -> name.append("Unknown Category Type");
        }

        assert category != null;
        name.append(" - ").append(category.getGender());
        name.append(" - ").append(category.getAgeGroup().name().replace("_", " "));

        switch (category) {
            case KataIndividualCategory kataIndividualCategory -> name.append(" - ").append(kataIndividualCategory.getKataBeltRange().name().replace("_", " "));
            case KumiteIndividualCategory kumiteIndividualCategory -> name.append(" - ").append(kumiteIndividualCategory.getKumiteDivisionRange().name().replace("_", " "));
            case KataTeamCategory kataTeamCategory -> name.append(" - ").append(kataTeamCategory.getKataTeamType().name().replace("_", " "));
            case KumiteTeamCategory kumiteTeamCategory -> name.append(" - ").append(kumiteTeamCategory.getKumiteTeamType().name().replace("_", " "));
            default -> name.append("Unknown Category Type");
        }
        
        return name.toString();
    }
} 