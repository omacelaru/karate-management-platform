package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.individual.KataIndividualCategory;

/**
 * DTO for {@link KataIndividualCategory}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class KataCategoryResponse extends CategoryResponse {
    private KataBeltRange kataBeltRange;
}