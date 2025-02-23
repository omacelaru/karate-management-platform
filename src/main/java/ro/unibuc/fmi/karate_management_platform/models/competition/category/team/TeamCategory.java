package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class TeamCategory extends Category {
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "team_category_teams",
            joinColumns = @JoinColumn(name = "team_category_id", referencedColumnName = "id", table = "category"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams = new LinkedHashSet<>();
}
