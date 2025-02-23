package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryType;

public enum MatchType {
    INDIVIDUAL_KUMITE,
    INDIVIDUAL_KATA,
    TEAM_KUMITE,
    TEAM_KATA;

    public CategoryType getCategoryType() {
        return switch (this) {
            case INDIVIDUAL_KUMITE, TEAM_KUMITE -> CategoryType.KATA_INDIVIDUAL;
            case INDIVIDUAL_KATA, TEAM_KATA -> CategoryType.KUMITE_INDIVIDUAL;
        };
    }
}
