package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategoryType;

/**
 * DTO for {@link ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategory}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class KataCategoryResponse extends CategoryResponse {
    private KataCategoryType kataCategoryType;
}