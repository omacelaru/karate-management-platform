package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class KumiteCategoryResponse extends CategoryResponse {
    private Short weightMin;
    private Short weightMax;
}
