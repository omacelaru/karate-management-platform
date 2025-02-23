package ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.team;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class KumiteTeamCategory extends Category {
    private KumiteTeamType kumiteTeamType;
}
