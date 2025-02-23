package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.team;

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
public class KataTeamCategory extends Category {
    private KataTeamType kataTeamType;
}
